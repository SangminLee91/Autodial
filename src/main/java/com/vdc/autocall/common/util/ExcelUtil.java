package com.vdc.autocall.common.util;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.jxls.exception.ParsePropertyException;
import net.sf.jxls.transformer.XLSTransformer;

import java.io.*;
import java.util.*;

@Component("excelUtil")
public class ExcelUtil {

    public Workbook getWorkbook(HttpServletRequest request) throws IOException {
        MultipartHttpServletRequest multipartHttpServletRequest = (MultipartHttpServletRequest)request;
        Iterator<String> iterator = multipartHttpServletRequest.getFileNames();

        MultipartFile file = null;
        String fileName;
        String fileExtension = null;

        while(iterator.hasNext()) {
            file = multipartHttpServletRequest.getFile(iterator.next());
            if (!file.isEmpty()) {
                fileName = file.getOriginalFilename();
                fileExtension = fileName.substring(fileName.lastIndexOf("."));
            }
        }

        Workbook workbook = null;
        if (fileExtension.equals("xlsx")) {
            workbook = new XSSFWorkbook(file.getInputStream());
        } else if (fileExtension.equals("xls")) {
            workbook = new HSSFWorkbook(file.getInputStream());
        }
        return workbook;
    }



    public List<Map<String,Object>> ExcelRead(HttpServletRequest request) throws IOException {

        Workbook workbook = getWorkbook(request);
        Sheet worksheet = workbook.getSheetAt(0);

        List<Map<String,Object>> dataList = new ArrayList<>();

        for (int i = 1; i < worksheet.getPhysicalNumberOfRows(); i++) {

            Map<String, Object> data = new HashMap<>();

            Row row = worksheet.getRow(i);
            data.put("col1", row.getCell(0).getStringCellValue());
            data.put("col2", row.getCell(1).getStringCellValue());
            data.put("col3", row.getCell(2).getStringCellValue());

            dataList.add(data);
        }

        return dataList;
    }

    // Excel DownLoad
    public void download(HttpServletRequest request, HttpServletResponse response,
                         Map<String, Object> bean, String fileName, String templateFile)
            throws ParsePropertyException, InvalidFormatException {

        String tempPath = request.getSession().getServletContext().getRealPath("/WEB-INF/template");

        try {

            InputStream is = new BufferedInputStream(new FileInputStream(tempPath + "//" + templateFile));
            XLSTransformer xls = new XLSTransformer();
            Workbook workbook = xls.transformXLS(is, bean);

            response.setHeader("Content-Disposition", "attachment; filename=\"" + fileName + ".xlsx\"");

            OutputStream os = response.getOutputStream();
            workbook.write(os);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
