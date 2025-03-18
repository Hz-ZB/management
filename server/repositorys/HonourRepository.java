package cn.edu.sdu.java.server.repositorys;

import cn.edu.sdu.java.server.models.Honour;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/*
荣誉管理接口，主要实现荣誉的查找、分类工作
 */

@Repository
public interface HonourRepository extends JpaRepository<Honour,Integer>{

    @Query(value="from Honour where person.personId=?1")
    List<Honour> findByPersonId(Integer personId);
/*
    @Query(value="from Honour where (?1=0 or person.personId=?1) and (?2=0 or honour.honourId=?2)" )
    List<Honour> findByPersonHonour(Integer personId, Integer honourId);

    @Query(value="from Honour where person.personId=?1 and (?2=0 or honour.name like %?2%)" )
    List<Honour> findByPersonHonour(Integer personId, String honourName);



    @Query(value="from Honour where person.personId=?1 and (?2=0 or honour.type=?2)" )
    List<Honour> findByPersonType(Integer personId,String personType);
*/

}
