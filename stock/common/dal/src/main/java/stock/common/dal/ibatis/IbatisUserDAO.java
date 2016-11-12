package stock.common.dal.ibatis;

import org.mybatis.spring.support.SqlSessionDaoSupport;
import stock.common.dal.datainterface.UserDAO;
import stock.common.dal.dataobject.User;

import java.util.List;

/**
 * Created by songyuanren on 2016/10/18.
 */
public class IbatisUserDAO extends SqlSessionDaoSupport implements UserDAO {

    public List<User> findUsers() {
        return getSqlSession().selectList("MS-USER-QUERY-ALL");
    }
}
