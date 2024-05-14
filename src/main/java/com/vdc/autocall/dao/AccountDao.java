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
    public boolean isUserIdExists(String userId) {
        // 중복 아이디 조회 쿼리 실행
        int count = sqlSession.selectOne("Account.isUserIdExists", userId);
        // count가 0보다 크면 중복된 아이디 존재
        return count > 0;
    }
    public void DelAccount(Map<String, Object> map) {
        delete(sqlSession, "Account.AccountDel", map);
    }

    public Map<String, Object> account_login(Map<String, Object> map){
        return (Map<String, Object>)selectOne(sqlSession, "Account.account_login", map);
    }
}
