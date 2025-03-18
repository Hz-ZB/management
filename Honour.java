package cn.edu.sdu.java.server.models;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;

/*
荣誉实体类：存储荣誉，需要与学生或教师关联，包括：
荣誉序号Integer类
关联人物Person实体
人物类型String（便于后续添加通过人员类型筛选某类人群的奖项的功能) 1学生2教师
荣誉名称String类
荣誉类型String类（国家级，省级，市级，校级，院级）
荣誉等第Integer类

*/
import lombok.Getter;
import lombok.Setter;
@Getter
@Setter
@Entity
@Table(	name = "honour",
        uniqueConstraints = {
        })

public class Honour {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer honourId;

    @ManyToOne
    @JoinColumn(name = "personId")
    private Person person;

    @Size(max = 2)
    private String type;

    @Size(max = 30)
    private String name;

    @Size(max=5)
    private String level;

    private Integer mark;
}
