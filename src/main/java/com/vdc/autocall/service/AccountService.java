package com.vdc.autocall.service;

import com.vdc.autocall.dao.AccountDao;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
@Service("accountService")
public class AccountService {
    @Resource(name="accountDao")
    private AccountDao accountDao;
    public List<Map<String, Object>> AccountList(Map<String, Object> map) throws Exception {
        return accountDao.AccountList(map);
    }
    public void addAccount(Map<String, Object> map) throws Exception {
        String userId = (String) map.get("user_id");
        if (accountDao.isUserIdExists(userId)) {
            throw new Exception("이미 존재하는 사용자 아이디입니다.");
        }
        accountDao.AddAccount(map);
    }
    public void delAccount(Map<String, Object> map) throws Exception {
        accountDao.DelAccount(map);
    }

    public Map<String, Object> account_login(Map<String, Object> map) throws Exception {
        return accountDao.account_login(map);
    }

}
