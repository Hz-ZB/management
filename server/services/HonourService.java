package cn.edu.sdu.java.server.services;

import cn.edu.sdu.java.server.models.Honour;
import cn.edu.sdu.java.server.models.Person;
import cn.edu.sdu.java.server.payload.request.DataRequest;
import cn.edu.sdu.java.server.payload.response.DataResponse;
import cn.edu.sdu.java.server.payload.response.OptionItem;
import cn.edu.sdu.java.server.payload.response.OptionItemList;
import cn.edu.sdu.java.server.repositorys.HonourRepository;
import cn.edu.sdu.java.server.repositorys.PersonRepository;
import cn.edu.sdu.java.server.util.CommonMethod;
import org.springframework.stereotype.Service;
import java.util.*;

/*
荣誉管理服务
增删查
 */

@Service
public class HonourService {
    private final HonourRepository honourRepository;
    private final PersonRepository personRepository;

    public HonourService(HonourRepository honourRepository, PersonRepository personRepository){
        this.honourRepository=honourRepository;
        this.personRepository=personRepository;
    }

    public OptionItemList getPersonItemOptionList( DataRequest dataRequest) {
        List<Person> sList = personRepository.findPersonListByNumName("");  //数据库查询操作
        List<OptionItem> itemList = new ArrayList<>();
        for (Person p : sList) {
            itemList.add(new OptionItem( p.getPersonId(),p.getPersonId()+"", p.getNum()+"-"+p.getName()));
        }
        return new OptionItemList(0, itemList);
    }

    public OptionItemList getHonourItemOptionList(DataRequest dataRequest) {
        List<Honour> sList = honourRepository.findAll();  //数据库查询操作
        List<OptionItem> itemList = new ArrayList<>();
        for (Honour h : sList) {
            itemList.add(new OptionItem(h.getHonourId(),h.getHonourId()+"", h.getLevel()+"-"+h.getName()));
        }
        return new OptionItemList(0, itemList);
    }

    public DataResponse getHonourList(DataRequest dataRequest) {
        Integer personId = dataRequest.getInteger("personId");
        if(personId == null)
            personId = 0;
        List<Honour> sList = honourRepository.findByPersonId(personId);  //数据库查询操作
        List<Map<String,Object>> dataList = new ArrayList<>();
        Map<String,Object> m;
        for (Honour h : sList) {
            m = new HashMap<>();
            m.put("honourId", h.getHonourId()+"");
            m.put("honourName",h.getLevel()+"-"+h.getName());
            m.put("honourMark",h.getMark());
            m.put("personId",h.getPerson().getPersonId()+"");
            m.put("personNum",h.getPerson().getNum());
            m.put("personName",h.getPerson().getName());
            dataList.add(m);
        }
        return CommonMethod.getReturnData(dataList);
    }

    public DataResponse honourSave(DataRequest dataRequest) {
        Integer personId = dataRequest.getInteger("personId");
        Integer honourId = dataRequest.getInteger("honourId");
        Optional<Honour> op;
        Honour h = null;
        if(honourId != null) {
            op= honourRepository.findById(honourId);
            if(op.isPresent())
                h = op.get();
        }
        if(h == null) {
            h = new Honour();
            h.setPerson(personRepository.findById(personId).get());
        }
        honourRepository.save(h);
        return CommonMethod.getReturnMessageOK();
    }
    public DataResponse honourDelete(DataRequest dataRequest) {
        Integer honourId = dataRequest.getInteger("honourId");
        Optional<Honour> op;
        Honour h = null;
        if(honourId != null) {
            op= honourRepository.findById(honourId);
            if(op.isPresent()) {
                h = op.get();
                honourRepository.delete(h);
            }
        }
        return CommonMethod.getReturnMessageOK();
    }


}
