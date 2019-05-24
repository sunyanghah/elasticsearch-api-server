package cn.com.chinaunicom.constant;

import lombok.Data;

/**
 * Created by dell on 2018/7/29.
 * @author dell
 */
@Data
public class ElasticDoc {
    private Object doc;

    public ElasticDoc(){}

    public ElasticDoc(Object object){
        this.doc = object;
    }

}
