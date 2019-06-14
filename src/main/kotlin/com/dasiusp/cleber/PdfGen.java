import com.itextpdf.text.Document;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;
import java.io.FileOutputStream;
import java.util.regex.Pattern;

// CLASSE GERADORA DE PDF.
// A SAÍDA É GERADA NA PASTA OUT/PRODUCTION/<NOMEDASUACLASSE>
public class PdfGen {
    public static void main(String[] args) {

        // CHAMADA DO MÉTODO PARA GERAR O PDF COM O DEVIDO NÚMERO USP COMO NOME DO ARQUIVO
        // EX.: 9390378.pdf
        boolean resp = generatePDF("9390378");

        // SE RETORNOU TRUE, ELE CRIOU E DEU TUDO CERTO
        if (resp) System.out.println("Deu certo, caralho");

        // DO CONTRÁRIO, É ISSO AÍ
        else System.out.println("Ixi tiu, revê essas fita");
    }

    // MÉTODO PARA GERAR O PDF NA PASTA (RETORNA TRUE SE CRIOU CORRETAMENTE E A MENSAGEM DE ERRO CASO FALHOU)
    public static boolean generatePDF(String nusp) {
        Document document = new Document();
        try {
            String dir = System.getProperty("user.dir");
            String[] endDir = dir.split(Pattern.quote("\\"));
            dir += "\\out\\production\\" + endDir[endDir.length-1] + "\\" + nusp + ".pdf";
            dir = dir.replace("\\", "\\\\");
            PdfWriter.getInstance(document, new FileOutputStream(dir));
            document.open();
            document.add(new Paragraph("Vai toma no cu"));
            document.close();
        }
        catch (Exception e) {
            System.out.println(e);
        }
        return true;
    }
}