package stock.common.dal.datainterface;

import stock.common.dal.dataobject.User;

import java.util.List;

/**
 * Created by songyuanren on 2016/10/18.
 */
public interface UserDAO {

    public List<User> findUsers();
}
