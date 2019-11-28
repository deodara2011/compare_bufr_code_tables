package org.nmic.diff_bufr_flag_tables;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        String basePath = "E:\\NMIC\\common\\bufr\\wmotables\\";
        String baseTableFileName = basePath + "in\\va_BUFRCREX_23_0_0_CodeFlag_en.txt";
        String compTableFileName = basePath + "in\\va_BUFRCREX_32_0_0_CodeFlag_en.txt";
        String outPath = basePath + "out\\";
        int len = args.length;
        if (len > 0){
            baseTableFileName = args[0];
        }
        if (len > 1){
            compTableFileName = args[1];
        }
        if (len > 2){
            outPath = args[2];
        }
        
        Comparator comp = new Comparator(baseTableFileName, compTableFileName, outPath);
        comp.compare();
        
        System.out.println( "Done." );
    }
}
