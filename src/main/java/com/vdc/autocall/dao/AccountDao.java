package com.vdc.autocall.dao;

import com.vdc.autocall.common.dao.AbstractDAO;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;


@Repository("accountDao")
public class AccountDao extends AbstractDAO{
    @Resource(name="sqlSessionMain")
    private SqlSessionTemplate sqlSession;
    @SuppressWarnings("unchecked")
    public List<Map<String, Object>> AccountList(Map<String, Object> map) {
        return (List<Map<String, Object>>)selectList(sqlSession, "Account.AccountList", map);
    }
    public void AddAccount(Map<String, Object> map) {
        insert(sqlSession, "Account.AccountAdd", map);
    }
    public void DelAccount(Map<String, Object> map) {
        delete(sqlSession, "Account.AccountDel", map);
    }

    public Map<String, Object> account_login(Map<String, Object> map){
        return (Map<String, Object>)selectOne(sqlSession, "Account.account_login", map);
    }

}
