package com.sastabackend.service.user;

import com.sastabackend.domain.ResponseModel;
import com.sastabackend.domain.Session;
import com.sastabackend.domain.Users;
import com.sastabackend.repository.UserRepository;
import com.sastabackend.service.user.exception.UserAlreadyExistsException;
import com.sastabackend.util.TextUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.SqlOutParameter;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.apache.commons.lang.StringUtils;
import javax.inject.Inject;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.List;
import java.util.HashMap;
import java.util.Map;

@Service
@Validated
public class UserServiceImpl implements UserService {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private static final Logger LOGGER = LoggerFactory.getLogger(UserServiceImpl.class);
    private final UserRepository repository;

    @Inject
    public UserServiceImpl(final UserRepository repository) {
        this.repository = repository;
    }

    @Override
    @Transactional
    public Users save(@NotNull @Valid final Users users) {
        LOGGER.debug("Creating {}", users);
        Users existing = repository.findOne(users.getId());
        if (existing != null) {
            throw new UserAlreadyExistsException(
                    String.format("There already exists a users with id=%s", users.getId()));
        }
        return repository.save(users);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Users> getList() {
        LOGGER.debug("Retrieving the list of all users");
        return repository.findAll();
    }

    @Override
    public ResponseModel SignIn(String email,String password){
        ResponseModel response = null;
        try {
            response = new ResponseModel<Session>();
            password = TextUtil.makeOneWayPasswordWithMD5(password);
            LOGGER.debug("Received request to Sign in " + email,password);
            response.setData(doSignIn(email, password));
            response.setStatus(true);
            return response;
        }catch(Exception err){
            response = new ResponseModel<String>();
            response.setData(err.getMessage());
        }
        return response;
    }

    /**
     * Logging in is usually used to enter a specific page, which trespassers cannot see. Once
     * the user is logged in, the login token may be used to track what actions the user has taken
     * while connected to the site
     * @param email - name of the registered user email
     * @param password - secured key values for appropriate account
     * @return String - Will provide you a session Id. If its null the current user Account is not valid
     */
    private Session doSignIn(String email, String password){
        SimpleJdbcCall simplejdbcCall = new SimpleJdbcCall(jdbcTemplate).withProcedureName("signin")
                .declareParameters(
                        new SqlParameter("uname", Types.VARCHAR),
                        new SqlParameter("pwd", Types.VARCHAR),
                        new SqlParameter("utctime", Types.INTEGER),
                        new SqlOutParameter("sessionid", Types.VARCHAR)
                );
        Map<String, Object> inParamMap = new HashMap<String, Object>();
        inParamMap.put("uname", email);
        inParamMap.put("pwd", password);
        inParamMap.put("utctime", 1);
        SqlParameterSource paramMap = new MapSqlParameterSource(inParamMap);
        simplejdbcCall.compile();
        Map<String, Object> simpleJdbcCallResult = simplejdbcCall.execute(paramMap);
        if(!simpleJdbcCallResult.isEmpty())
            return getUserSession(simpleJdbcCallResult.get("sessionid").toString());
        else
            return null;
    }

    @Override
    public ResponseModel Add(Users users){

        ResponseModel response = null;
        try {
            response = new ResponseModel<Long>();
            SimpleJdbcCall simplejdbcCall = new SimpleJdbcCall(jdbcTemplate).withProcedureName("insert_users")
                    .declareParameters(
                            new SqlParameter("email", Types.VARCHAR),
                            new SqlParameter("pwd", Types.VARCHAR),
                            new SqlParameter("screenname", Types.VARCHAR),
                            new SqlParameter("firstname", Types.VARCHAR),
                            new SqlParameter("lastname", Types.VARCHAR),
                            new SqlParameter("genderid", Types.INTEGER),
                            new SqlParameter("jobtitle", Types.INTEGER),
                            new SqlParameter("hasreadtc", Types.BIT),
                            new SqlParameter("stateid", Types.INTEGER),
                            new SqlParameter("countryid", Types.INTEGER),
                            new SqlParameter("usergroupid", Types.INTEGER),
                            new SqlParameter("dateofjoining", Types.DATE),
                            new SqlParameter("teamname", Types.VARCHAR),
                            new SqlParameter("employeeid", Types.VARCHAR),
                            new SqlParameter("departmentid", Types.INTEGER),
                            new SqlParameter("reportingid", Types.INTEGER),
                            new SqlParameter("allotteddistrict", Types.INTEGER),
                            new SqlParameter("allottedblock", Types.INTEGER),
                            new SqlParameter("recruitmentid", Types.INTEGER),
                            new SqlParameter("createdby", Types.BIGINT),
                            new SqlOutParameter("userid", Types.BIGINT)
                    );
            Map<String, Object> inParamMap = new HashMap<String, Object>();
            inParamMap.put("email", users.getEmail());
            inParamMap.put("pwd", users.getPassword());
            inParamMap.put("screenname", users.getScreenName());
            inParamMap.put("firstname", users.getFirstName());
            inParamMap.put("lastname", users.getLastName());
            inParamMap.put("genderid", users.getGenderId());
            inParamMap.put("jobtitle", users.getJobTitle());
            inParamMap.put("hasreadtc", users.getHasReadTermsAndCondtion());
            inParamMap.put("stateid", users.getStateId());
            inParamMap.put("countryid", users.getCountryId());
            inParamMap.put("usergroupid", users.getUserGroupId());
            inParamMap.put("dateofjoining", users.getDateOfJoining());
            inParamMap.put("teamname", users.getTeamName());
            inParamMap.put("employeeid", users.getEmployeeId());
            inParamMap.put("departmentid", users.getDepartmentId());
            inParamMap.put("reportingid", users.getReportingId());
            inParamMap.put("allotteddistrict", users.getAllottedDistrict());
            inParamMap.put("allottedblock", users.getAllottedBlock());
            inParamMap.put("recruitmentid", users.getRecruitmentId());
            inParamMap.put("createdby", users.getCreatedBy());
            SqlParameterSource paramMap = new MapSqlParameterSource(inParamMap);
            simplejdbcCall.compile();
            Map<String, Object> simpleJdbcCallResult = simplejdbcCall.execute(paramMap);
            if (!simpleJdbcCallResult.isEmpty())
                response.setData((Long) (simpleJdbcCallResult.get("userid")));
            response.setStatus(true);
        }catch(Exception err){
            response = new ResponseModel<String>();
            response.setStatus(false);
            response.setData(err.getMessage());
        }
        return response;
    }
    /**
     * Read session details current user login
     * @param session
     * @return
     */
    private Session getUserSession(String session){
        if(session.isEmpty()){
            throw  new RuntimeException("unable to read user session!");
        }
        try {
            System.out.println(!session.isEmpty() ? session : "empty");
            useSimpleJdbcCall(session);
            List list = jdbcTemplate.query("call select_session(?)", new Object[]{session}, new SessionMapper());
            if (!CollectionUtils.isEmpty(list)) {
                Session o=(Session) list.get(0);
                return o;
            }
            else
             return null;
        }catch(Exception err){
            System.out.println(err.getMessage());
        }
        return null;
    }

    private Session useSimpleJdbcCall(String sessionID) {

        SimpleJdbcCall simpleJdbcCall = new SimpleJdbcCall(jdbcTemplate).withProcedureName("select_session");

        Map<String, Object> inParamMap = new HashMap<String, Object>();
        inParamMap.put("sessionid", sessionID);
        SqlParameterSource in = new MapSqlParameterSource(inParamMap);

        Map<String, Object> simpleJdbcCallResult = simpleJdbcCall.execute(in);
        Session o = new Session();
        o.setCreatedDate(StringUtils.trimToNull((String) simpleJdbcCallResult.get("create_date")));
        o.setExpiredDate(StringUtils.trimToNull((String) simpleJdbcCallResult.get("expire_date")));
        o.setUserFullName(StringUtils.trimToNull((String) simpleJdbcCallResult.get("user_full_name")));
        o.setScreenName(StringUtils.trimToNull((String) simpleJdbcCallResult.get("screen_name")));
        o.setEmployeeID(StringUtils.trimToNull((String) simpleJdbcCallResult.get("user_group_id")));
        o.setCountryId((Integer) simpleJdbcCallResult.get("employee_id"));
        o.setReportingId((Long) simpleJdbcCallResult.get("country_id"));
        o.setReportingId((Long) simpleJdbcCallResult.get("reporting_id"));
        System.out.println(o.toString());
        return o;
    }
    protected static final class SessionMapper implements RowMapper{

        public Object mapRow(ResultSet set, int rowNo)throws SQLException {
            System.out.println("Read Row :" + rowNo);
            Session o = new Session();
            o.setSessionId(StringUtils.trimToNull(set.getString("session_id")));
            o.setUserId(set.getLong("user_id"));
            o.setCreatedDate(StringUtils.trimToNull(set.getString("create_date")));
            o.setExpiredDate(StringUtils.trimToNull(set.getString("expire_date")));
            o.setUserFullName(StringUtils.trimToNull(set.getString("user_full_name")));
            o.setScreenName(StringUtils.trimToNull(set.getString("screen_name")));
            o.setEmployeeID(StringUtils.trimToNull(set.getString("employee_id")));
            o.setCountryId(set.getInt("country_id"));
            o.setReportingId(set.getLong("reporting_id"));
            System.out.println(o.toString());
            return o;
        }
    }
}