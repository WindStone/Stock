package stock.common.dal.ibatis;

import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;
import stock.common.dal.datainterface.UserDAO;
import stock.common.dal.dataobject.User;

import java.util.List;

/**
 * Created by songyuanren on 2016/10/18.
 */
public class IbatisUserDAO extends SqlMapClientDaoSupport implements UserDAO {

    public List<User> findUsers() {
        return getSqlMapClientTemplate().queryForList("MS-USER-QUERY-ALL");
    }
}
