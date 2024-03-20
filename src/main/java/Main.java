import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.*;


import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class Main {
    public static String PDF_PATH = "D:\\pdf\\temp.pdf";
    /**
     * @description 追加pdf  在headReader后面追加tailReader
     * @param headReader PdfReader
     * @param tailReader PdfReader
     * @return out
     */
    public static FileOutputStream pdfFollowPdf(PdfReader headReader, PdfReader tailReader){
        //创建文档
        Document document = new Document();
        PdfWriter writer;
        FileOutputStream out = null;
        try {
            //最终文件输出
            out = new FileOutputStream(PDF_PATH);
            writer = PdfWriter.getInstance(document, out);
            //打开文档
            document.open();
            PdfContentByte cb = writer.getDirectContent();

            int currentPage=1;
            while(currentPage<=headReader.getNumberOfPages()){
                document.newPage();
                //获取某一页
                PdfImportedPage page = writer.getImportedPage(headReader, currentPage);
                cb.addTemplate(page, 0, 0);
                currentPage++;
            }

            currentPage=1;
            while(currentPage<=tailReader.getNumberOfPages()){
                document.newPage();
                //获取某一页
                PdfImportedPage page = writer.getImportedPage(tailReader, currentPage);
                cb.addTemplate(page, 0, 0);
                currentPage++;
            }
            document.close();
            out.flush();
            out.close();
        } catch (DocumentException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return out;
    }

    /**
     * @description 插入pdf 指定序列号下
     * @param headReader PdfReader
     * @param tailReader PdfReader
     * @return out FileOutputStream
     */
    public static FileOutputStream pdfIntoPdf(PdfReader headReader,PdfReader tailReader,int insertPage){
        if(insertPage>headReader.getNumberOfPages()+1){
            System.out.println("需插入的页号超过总页数");
            return null;
        }
        FileOutputStream out = null;
        Document document = new Document();
        PdfWriter writer;
        //最终文件输出
        try {
            out = new FileOutputStream(PDF_PATH);
            writer = PdfWriter.getInstance(document, out);
            document.open();
            PdfContentByte cb = writer.getDirectContent();
            int headCurrentPage=1;
            int tailCurrentPage=1;
            int currentPage=1;
            while(currentPage<=headReader.getNumberOfPages()+tailReader.getNumberOfPages()){
                document.newPage();
                if(currentPage==insertPage){
                    while(tailCurrentPage<=tailReader.getNumberOfPages()){
                        //获取某一页
                        PdfImportedPage page = writer.getImportedPage(tailReader, tailCurrentPage);
                        cb.addTemplate(page, 0, 0);
                        tailCurrentPage++;
                        currentPage++;
                    }
                }else{
                    //获取某一页
                    PdfImportedPage page = writer.getImportedPage(headReader, headCurrentPage);
                    cb.addTemplate(page, 0, 0);
                    headCurrentPage++;
                    currentPage++;
                }
            }
            document.close();
            out.flush();
            out.close();
        } catch (DocumentException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return out;
    }

    /**
     * @description 删除指定页的pdf
     * @param headReader PdfReader
     * @param deletePage int
     * @return out FileOutputStream
     */
    public static FileOutputStream delPdfByIndex(PdfReader headReader,int deletePage){
        if(deletePage>headReader.getNumberOfPages()){
            System.out.println("需删除的页号超过总页数");
            return null;
        }
        //处理需要的页数
        StringBuffer showPages=new StringBuffer("");
        for(int i=1;i<=headReader.getNumberOfPages();i++){
            if(i!=deletePage){
                showPages.append(",").append(i);
            }
        }
        //去除第一个逗号
        showPages.delete(0, 1);
        FileOutputStream out = null;
        try {
            out = new FileOutputStream(PDF_PATH);
            headReader.selectPages(showPages.toString());
            PdfStamper stamp;
            stamp = new PdfStamper(headReader,out);
            stamp.close();
            out.flush();
            out.close();
        } catch (DocumentException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return out;
    }

    /**
     * @description 交换pdf（headIndex页交换tailIndex页）
     * @param headReader PdfReader
     * @param headIndex int
     * @param tailIndex int
     * @return out FileOutputStream
     */
    public static FileOutputStream pdfChangePage(PdfReader headReader,int headIndex,int tailIndex){
        if(headIndex>headReader.getNumberOfPages()||tailIndex>headReader.getNumberOfPages()){
            System.out.println("需交换的页号超过总页数");
            return null;
        }

        Document document = new Document();
        PdfWriter writer;
        FileOutputStream out = null;
        try {
            out = new FileOutputStream(PDF_PATH);
            writer = PdfWriter.getInstance(document, out);
            document.open();
            PdfContentByte cb = writer.getDirectContent();

            int currentPage=1;
            while(currentPage<=headReader.getNumberOfPages()){
                document.newPage();
                if(currentPage==headIndex){
                    //获取某一页
                    PdfImportedPage page = writer.getImportedPage(headReader, tailIndex);
                    cb.addTemplate(page, 0, 0);
                    currentPage++;
                }else if(currentPage==tailIndex){
                    //获取某一页
                    PdfImportedPage page = writer.getImportedPage(headReader, headIndex);
                    cb.addTemplate(page, 0, 0);
                    currentPage++;
                }else{
                    //获取某一页
                    PdfImportedPage page = writer.getImportedPage(headReader, currentPage);
                    cb.addTemplate(page, 0, 0);
                    currentPage++;
                }
            }
            document.close();
            out.flush();
            out.close();
        } catch (DocumentException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return out;
    }
    public static void extractPages(String src, String dest, int startPage, int endPage) throws IOException, DocumentException {
        PdfReader reader = new PdfReader(src);
        Document document = new Document();
        PdfCopy copy = new PdfCopy(document, new FileOutputStream(dest));
        document.open();
        for (int page = startPage; page <= endPage; page++) {
            PdfImportedPage importedPage = copy.getImportedPage(reader, page);
            copy.addPage(importedPage);
        }
        document.close();
    }

    public static void main(String[] args) {
        String sourcePdf = "D:\\OneDrive\\文档\\WeChat Files\\wxid_cumzm8z9xng522\\FileStorage\\File\\2024-03\\OpenModelicaSystem(2).pdf"; // 源 PDF 文件路径
        String destinationPdf = "output.pdf"; // 输出 PDF 文件路径
        int startPage = 19; // 开始页码
        int endPage = 45; // 结束页码

        try {
            extractPages(sourcePdf, destinationPdf, startPage, endPage);
            System.out.println("PDF 提取完成！");
        } catch (IOException | DocumentException e) {
            e.printStackTrace();
        }
    }
}
