package org.huihui.lib.realm;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * User: Administrator
 * Date: 2017-01-09 {HOUR}:59
 */
public class User extends RealmObject {
    @PrimaryKey
    String UID;
    int age;
    String name;

    public int getAge() {
        return age;
    }

    public String getUID() {
        return UID;
    }

    public void setUID(String UID) {
        this.UID = UID;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}