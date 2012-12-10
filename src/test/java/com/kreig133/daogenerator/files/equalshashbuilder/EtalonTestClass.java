package com.kreig133.daogenerator.files.equalshashbuilder;

import java.util.ArrayList;

/**
 * @author zildarius
 * @version 1.0
 */
public class EtalonTestClass {
    Byte    paramByte;
    Short   paramShort;
    Integer paramInt;
    Long    paramLong;

    Float   paramFloat;
    Double  paramDouble;

    String  paramString;

    public static String getEtalonClassName(){
        return "EtalonTestClass";
    }

    public static ArrayList<String> getParamList(){
        ArrayList<String> resultList = new ArrayList<String>();
        resultList.add("paramByte");
        resultList.add("paramShort");
        resultList.add("paramInt");
        resultList.add("paramLong");
        resultList.add("paramFloat");
        resultList.add("paramDouble");
        resultList.add("paramString");
        return resultList;
    }

    // See below
    public static final String GENERATED_HASHCODE_METHOD =
                "    @Override\n" +
                "    public int hashCode() {\n" +
                "        int result = paramByte != null ? paramByte.hashCode() : 0;\n" +
                "        result = 31 * result + (paramShort != null ? paramShort.hashCode() : 0);\n" +
                "        result = 31 * result + (paramInt != null ? paramInt.hashCode() : 0);\n" +
                "        result = 31 * result + (paramLong != null ? paramLong.hashCode() : 0);\n" +
                "        result = 31 * result + (paramFloat != null ? paramFloat.hashCode() : 0);\n" +
                "        result = 31 * result + (paramDouble != null ? paramDouble.hashCode() : 0);\n" +
                "        result = 31 * result + (paramString != null ? paramString.hashCode() : 0);\n" +
                "        return result;\n" +
                "    }";

    // See below
    public static final String GENERATED_EQUALS_METHOD =
                "    @Override\n" +
                "    public boolean equals(Object o) {\n" +
                "        if (this == o) return true;\n" +
                "        if (!(o instanceof EtalonTestClass)) return false;\n" +
                "\n" +
                "        EtalonTestClass that = (EtalonTestClass) o;\n" +
                "\n" +
                "        if (paramByte != null ? !paramByte.equals(that.paramByte) : that.paramByte != null) return false;\n" +
                "        if (paramDouble != null ? !paramDouble.equals(that.paramDouble) : that.paramDouble != null) return false;\n" +
                "        if (paramFloat != null ? !paramFloat.equals(that.paramFloat) : that.paramFloat != null) return false;\n" +
                "        if (paramInt != null ? !paramInt.equals(that.paramInt) : that.paramInt != null) return false;\n" +
                "        if (paramLong != null ? !paramLong.equals(that.paramLong) : that.paramLong != null) return false;\n" +
                "        if (paramShort != null ? !paramShort.equals(that.paramShort) : that.paramShort != null) return false;\n" +
                "        if (paramString != null ? !paramString.equals(that.paramString) : that.paramString != null) return false;\n" +
                "\n" +
                "        return true;\n" +
                "    }";

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof EtalonTestClass)) return false;

        EtalonTestClass that = (EtalonTestClass) o;

        if (paramByte != null ? !paramByte.equals(that.paramByte) : that.paramByte != null) return false;
        if (paramDouble != null ? !paramDouble.equals(that.paramDouble) : that.paramDouble != null) return false;
        if (paramFloat != null ? !paramFloat.equals(that.paramFloat) : that.paramFloat != null) return false;
        if (paramInt != null ? !paramInt.equals(that.paramInt) : that.paramInt != null) return false;
        if (paramLong != null ? !paramLong.equals(that.paramLong) : that.paramLong != null) return false;
        if (paramShort != null ? !paramShort.equals(that.paramShort) : that.paramShort != null) return false;
        if (paramString != null ? !paramString.equals(that.paramString) : that.paramString != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = paramByte != null ? paramByte.hashCode() : 0;
        result = 31 * result + (paramShort != null ? paramShort.hashCode() : 0);
        result = 31 * result + (paramInt != null ? paramInt.hashCode() : 0);
        result = 31 * result + (paramLong != null ? paramLong.hashCode() : 0);
        result = 31 * result + (paramFloat != null ? paramFloat.hashCode() : 0);
        result = 31 * result + (paramDouble != null ? paramDouble.hashCode() : 0);
        result = 31 * result + (paramString != null ? paramString.hashCode() : 0);
        return result;
    }
}
