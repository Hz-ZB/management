package cn.edu.sdu.java.server.services;

import cn.edu.sdu.java.server.models.*;
import cn.edu.sdu.java.server.payload.request.DataRequest;
import cn.edu.sdu.java.server.payload.response.DataResponse;
import cn.edu.sdu.java.server.repositorys.*;
import cn.edu.sdu.java.server.util.ComDataUtil;
import cn.edu.sdu.java.server.util.CommonMethod;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.*;

@Service
public class TeacherService {
    private static final Logger log = LoggerFactory.getLogger(TeacherService.class);
    private final PersonRepository personRepository;  //人员数据操作自动注入
    private final TeacherRepository teacherRepository;  //学生数据操作自动注入
    private final UserRepository userRepository;  //学生数据操作自动注入
    private final UserTypeRepository userTypeRepository; //用户类型数据操作自动注入
    private final PasswordEncoder encoder;  //密码服务自动注入
    private final FeeRepository feeRepository;  //消费数据操作自动注入
    private final FamilyMemberRepository familyMemberRepository;
    private final SystemService systemService;

    public TeacherService(PersonRepository personRepository, TeacherRepository teacherRepository, UserRepository userRepository, UserTypeRepository userTypeRepository, PasswordEncoder encoder,  FeeRepository feeRepository, FamilyMemberRepository familyMemberRepository, SystemService systemService) {
        this.personRepository = personRepository;
        this.teacherRepository = teacherRepository;
        this.userRepository = userRepository;
        this.userTypeRepository = userTypeRepository;
        this.encoder = encoder;
        this.feeRepository = feeRepository;
        this.familyMemberRepository = familyMemberRepository;
        this.systemService = systemService;
    }

    //把信息融合
    public Map<String,Object> getMapFromTeacher(Teacher s) {
        Map<String,Object> m = new HashMap<>();
        Person p;
        if(s == null)
            return m;
        m.put("title",s.getTitle());
        m.put("degree",s.getDegree());
        p = s.getPerson();
        if(p == null)
            return m;
        m.put("personId", s.getPersonId());
        m.put("num",p.getNum());
        m.put("name",p.getName());
        m.put("dept",p.getDept());
        m.put("card",p.getCard());
        String gender = p.getGender();
        m.put("gender",gender);
        m.put("genderName", ComDataUtil.getInstance().getDictionaryLabelByValue("XBM", gender)); //性别类型的值转换成数据类型名
        m.put("birthday", p.getBirthday());  //时间格式转换字符串
        m.put("email",p.getEmail());
        m.put("phone",p.getPhone());
        m.put("address",p.getAddress());
        m.put("introduce",p.getIntroduce());
        return m;
    }

    //Java 对象的注入 我们定义的这下Java的操作对象都不能自己管理是由有Spring框架来管理的， StudentController 中要使用StudentRepository接口的实现类对象，
    // 需要下列方式注入，否则无法使用， studentRepository 相当于StudentRepository接口实现对象的一个引用，由框架完成对这个引用的赋值，
    // StudentController中的方法可以直接使用

    public List<Map<String,Object>> getTeacherMapList(String numName) {
        List<Map<String,Object>> dataList = new ArrayList<>();
        List<Teacher> sList = teacherRepository.findTeacherListByNumName(numName);  //数据库查询操作
        if (sList == null || sList.isEmpty())
            return dataList;
        for (Teacher teacher : sList) {
            dataList.add(getMapFromTeacher(teacher));
        }
        return dataList;
    }

    public DataResponse getTeacherList(DataRequest dataRequest) {
        String numName = dataRequest.getString("numName");
        List<Map<String,Object>> dataList = getTeacherMapList(numName);
        return CommonMethod.getReturnData(dataList);  //按照测试框架规范会送Map的list
    }

    public DataResponse teacherDelete(DataRequest dataRequest) {
        Integer personId = dataRequest.getInteger("personId");  //获取student_id值
        Teacher s = null;
        Optional<Teacher> op;
        if (personId != null && personId > 0) {
            op = teacherRepository.findById(personId);   //查询获得实体对象
            if(op.isPresent()) {
                s = op.get();
                Optional<User> uOp = userRepository.findById(personId); //查询对应该学生的账户
                //删除对应该学生的账户
                uOp.ifPresent(userRepository::delete);
                Person p = s.getPerson();
                teacherRepository.delete(s);    //首先数据库永久删除学生信息
                personRepository.delete(p);   // 然后数据库永久删除学生信息
            }
        }
        return CommonMethod.getReturnMessageOK();  //通知前端操作正常
    }

    public DataResponse getTeacherInfo(DataRequest dataRequest) {
        Integer personId = dataRequest.getInteger("personId");
        Teacher s = null;
        Optional<Teacher> op;
        if (personId != null) {
            op = teacherRepository.findById(personId); //根据学生主键从数据库查询学生的信息
            if (op.isPresent()) {
                s = op.get();
            }
        }
        return CommonMethod.getReturnData(getMapFromTeacher(s)); //这里回传包含学生信息的Map对象
    }

    public DataResponse teacherEditSave(DataRequest dataRequest) {
        Integer personId = dataRequest.getInteger("personId");
        Map<String,Object> form = dataRequest.getMap("form"); //参数获取Map对象
        String num = CommonMethod.getString(form, "num");  //Map 获取属性的值
        Teacher s = null;
        Person p;
        Optional<Teacher> op;
        if (personId != null) {
            op = teacherRepository.findById(personId);  //查询对应数据库中主键为id的值的实体对象
            if (op.isPresent()) {
                s = op.get();
            }
        }
        Optional<Person> nOp = personRepository.findByNum(num); //查询是否存在num的人员
        if (nOp.isPresent()) {
            if (s == null || !s.getPerson().getNum().equals(num)) {
                return CommonMethod.getReturnMessageError("新工号已经存在，不能添加或修改！");
            }
        }
        if (s == null) {
            p = new Person();
            p.setNum(num);
            p.setType("2");
            personRepository.save(p);  //插入新的Person记录
            personId = p.getPersonId();
            try{
                s=new Teacher();
                s.setPersonId(personId);
                teacherRepository.save(s);
            }catch(Exception e){
                e.printStackTrace();
            }
        } else {
            p = s.getPerson();
        }

        p.setName(CommonMethod.getString(form, "name"));
        p.setDept(CommonMethod.getString(form, "dept"));
        p.setCard(CommonMethod.getString(form, "card"));
        p.setGender(CommonMethod.getString(form, "gender"));
        p.setBirthday(CommonMethod.getString(form, "birthday"));
        p.setEmail(CommonMethod.getString(form, "email"));
        p.setPhone(CommonMethod.getString(form, "phone"));
        p.setAddress(CommonMethod.getString(form, "address"));
        personRepository.save(p);  // 修改保存人员信息
        s.setTitle(CommonMethod.getString(form, "title"));
        s.setDegree(CommonMethod.getString(form, "degree"));
        teacherRepository.save(s);  //修改保存学生信息
        return CommonMethod.getReturnData(s.getPersonId());  // 将personId返回前端
    }

}
