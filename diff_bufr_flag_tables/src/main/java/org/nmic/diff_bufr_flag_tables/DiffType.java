package org.nmic.diff_bufr_flag_tables;

public enum DiffType {
    ADD_ELE("增加要素"), RED_ELE("减少要素"),
    SAME("一致"), ADD_CODE("增加代码"), RED_CODE("减少代码"), DIFF_VAL("代码值含义不同");
    
    private String val;
    
    private DiffType(String val){
        this.val = val;
    }

    public String getVal() {
        return val;
    }
}
