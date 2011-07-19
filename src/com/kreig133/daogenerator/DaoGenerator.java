package com.kreig133.daogenerator;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author eshangareev
 * @version 1.0
 */
public class DaoGenerator {
    public static String[] splitIt(String input) {
        return input.split(";");
    }

    public static void main(String[] args) throws IOException {

        final File file = new File(OUTPUT_PATH);
        if(!file.exists()){
            file.mkdirs();
        }

        final File file1 = new File(OUTPUT_PATH_FOR_ENTITY);
        if(!file1.exists()){
            file1.mkdirs();
        }
        String path = "D:/ForGenerateDao";
        for(
                String s:
                (new File(path)).list(new FilenameFilter() {
                    public boolean accept(File dir, String name) {
                        return name.endsWith("txt");
                    }
                }
            )
        ) {
            controller(new File(path + "/"+s));
        }

    }

    public static void controller(File fileWithData) throws IOException {
        name = fileWithData.getName().split(".txt")[0];

        INPUT_PARAMETER_LIST.clear();
        OUTPUT_PARAMETER_LIST.clear();

        readFileWithDataForGenerateDao(fileWithData);

        if (!INPUT_PARAMETER_LIST.isEmpty()) {
            createJavaClassForInputOutputWrappers(INPUT_PARAMETER_LIST, Utils.convertNameForClassNaming(name) + "In");
        }

        if (!OUTPUT_PARAMETER_LIST.isEmpty()) {
            createJavaClassForInputOutputWrappers(OUTPUT_PARAMETER_LIST, Utils.convertNameForClassNaming(name) +
                    "Out");
        }

        appandByteToFile(new File(OUTPUT_PATH + "map.xml"), myBatisGenerator().getBytes());
        appandByteToFile(new File(OUTPUT_PATH + "interface.java"), methodGenerator().getBytes());
    }

    private static void createJavaClassForInputOutputWrappers(List<Parameter> parameterList,
                                                              String name) throws IOException {
        FileWriter writer = null;
        try {
            InOutClass inOutClass = new InOutClass(parameterList, name);

            File inClassFile = new File(OUTPUT_PATH_FOR_ENTITY + inOutClass.getName() + ".java");
            inClassFile.createNewFile();

            writer = new FileWriter(inClassFile);
            writer.write(inOutClass.toString());
        } finally {
            if (writer != null) {
                writer.close();
            }
        }
    }

    public static void appandByteToFile(File file, byte[] data) throws IOException {
        FileOutputStream writer = null;
        try {
            writer = new FileOutputStream(file, true);
            writer.write(data);
        } finally {
            if (writer != null) writer.close();
        }

    }

    public static String methodGenerator() {
        StringBuilder result = new StringBuilder();
        result.append("    ");
        if (!OUTPUT_PARAMETER_LIST.isEmpty()) {
            result.append(Utils.convertNameForClassNaming(name));
            result.append("Out ");
        } else {
            result.append("void ");
        }
        result.append(name);
        result.append("(");
        if (!INPUT_PARAMETER_LIST.isEmpty()) {
            result.append(Utils.convertNameForClassNaming(name));
            result.append("In request");
        }
        result.append(");\n");

        return result.toString();
    }

    public static String myBatisGenerator() {
        StringBuilder result = new StringBuilder();
        result.append("    <select id=\"");
        result.append(name);
        result.append("\" statementType=\"CALLABLE\"");
        if (!INPUT_PARAMETER_LIST.isEmpty()) {
            result.append("\n        parameterType=\"");
            result.append(InOutClass.PACKAGE);
            result.append(".");
            result.append(Utils.convertNameForClassNaming(name));
            result.append("In\"");
        }
        if (!OUTPUT_PARAMETER_LIST.isEmpty()) {
            result.append("\n        resultType=\"");
            result.append(InOutClass.PACKAGE);
            result.append(".");
            result.append(Utils.convertNameForClassNaming(name));
            result.append("Out\"");
        }
        result.append(">\n\n");
        result.append("        {CALL ");
        result.append(name);
        result.append("(\n");
        boolean  first = true;
        for (Parameter p : INPUT_PARAMETER_LIST) {
            if( !first ){
                result.append(",\n");
            } else first = false;
            result.append("            #{");
            result.append(p.getName().trim());
            result.append("}");
        }
        result.append("\n        )}\n\n");
        result.append("    </select>\n\n");

        return result.toString();
    }

    public static void readFileWithDataForGenerateDao(File fileWithData) throws IOException {

        final BufferedReader reader = new BufferedReader(
                new InputStreamReader(
                        new FileInputStream(fileWithData),
                        "CP1251"
                )
        );

        String line = reader.readLine();

        int j = 0; //TODO костыль обыкновенный

        while (line != null) {
            try{
                String[] params = splitIt(line); //TODO заменить эту фигню на что нибудь нормальное

                if( params.length < 2 ) { //TODO заменить провекри длины на что-то более вразумительное и поддерживаемое
                    line = reader.readLine(); // TODO слишком часто вызывается этот метод - не хорошо
                    continue;
                }

                //Это заголовок
                if (params[ 0 ].contains("№ пп")) { //TODO тоже костыль
                    j++;
                    line = reader.readLine();
                    continue;
                }

                if (j != 2) { //TODO
                    if (!(params[1] == null || "".equals(params[1]))) {
                        INPUT_PARAMETER_LIST.add(
                                new InputParameter(params[1], params[2],
                                        params.length >= 5 ? params[4] : null,
                                        params.length == 6 ? params[5] : null)
                        );
                    }

                    //Добавляем в комментарий кусок, который нашли на следующих линиях
                    for (int i = 0; i < 5; i++) {
                        if (!"".equals(params[i])) break;
                        if (i == 4 && !("".equals(params[5]))) {
                            final int indexLastElement = INPUT_PARAMETER_LIST.size() - 1;
                            INPUT_PARAMETER_LIST.get(indexLastElement).setComment(
                                    INPUT_PARAMETER_LIST.get(indexLastElement).getComment() + params[5]
                            );
                        }
                    }
                } else {

                    if (!(params[1] == null || "".equals(params[1]))) {
                        OUTPUT_PARAMETER_LIST.add(
                                new OutputParameter(
                                        params.length>3 ? params[3] : null,
                                        params[2], params[1])
                        );
                    }

                    //Добавляем в комментарий кусок, который нашли на следующих линиях
                    for ( int i = 0; i < 3; i++) {
                        if (!"".equals(params[i])) break;
                        if (i == 2 && !("".equals(params[3]))) {
                            final int indexLastElement = INPUT_PARAMETER_LIST.size() - 1;
                            INPUT_PARAMETER_LIST.get(indexLastElement).setComment(
                                    INPUT_PARAMETER_LIST.get(indexLastElement).getComment() + params[3]
                            );
                        }
                    }

                }
            } catch (ArrayIndexOutOfBoundsException ex){
                System.out.println(line);
                ex.printStackTrace();
                throw new RuntimeException(ex);
            }

            line = reader.readLine();
        }
    }

    private static String name;

    final private static List<Parameter> INPUT_PARAMETER_LIST = new ArrayList<Parameter>();
    final private static List<Parameter> OUTPUT_PARAMETER_LIST = new ArrayList<Parameter>();
    final private static String OUTPUT_PATH = "D:/GeneratedDao/";
    final private static String OUTPUT_PATH_FOR_ENTITY = OUTPUT_PATH + "Entity/";
}
