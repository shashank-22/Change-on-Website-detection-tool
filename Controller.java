import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import org.jsoup.Jsoup;

import java.io.*;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.ResourceBundle;

public class Controller implements Initializable
{
    int sl1,sl2,per;
    Connection con = db_connect.getcon();
    @FXML
    TextField sln1,sln2,pcb;
    @FXML
    Button cmp;
    @FXML
    Label prg;
    @Override
    public void initialize(URL location, ResourceBundle resources)
    {

    }


    public static String readUrl(URL url) throws IOException
    {
        BufferedReader br = null;
        StringBuilder sb = new StringBuilder();
        try
        {
            br = new BufferedReader(new InputStreamReader(url.openStream()));
            String line;
            while ((line = br.readLine()) != null)
            {

                sb.append(line);
                sb.append(System.lineSeparator());
            }
        }
        finally
        {
            if (br != null)
                br.close();
        }
        return sb.toString();
    }

    static String html2text(String html)
    {
        return Jsoup.parse(html).text();
    }

    public void cmp(ActionEvent actionEvent) throws SQLException, IOException
    {
        sl1 = Integer.parseInt(sln1.getText());
        sl2 = Integer.parseInt(sln2.getText());
        per = Integer.parseInt(pcb.getText());
        PreparedStatement stmt = con.prepareStatement("select * from site_list where sr_no>=? and sr_no<=?" );
        stmt.setInt(1,sl1);
        stmt.setInt(2,sl2);
        ResultSet rows = stmt.executeQuery();

        File fp_res = new File("C:\\Users\\Shashank\\IdeaProjects\\intern\\src\\Result.html");
        FileWriter fw_res = new FileWriter(fp_res);

        String kw="";
        File fp = new File("C:\\Users\\Shashank\\IdeaProjects\\intern\\src\\keyword.txt");
        FileReader fr = new FileReader(fp);
        int temp;
        try
        {
            while((temp=fr.read())!=-1)
                kw+=(char)temp;
            fr.close();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        String kw_ar[] = kw.split(" ");
        HashSet<String> keyword = new HashSet<String>();
        for(int i=0;i<kw_ar.length;i++)
        {
            keyword.add(kw_ar[i]);
        }
        while(rows.next())
        {
            System.out.println(rows.getString(1));
            int url_index = rows.getInt(1);
            String url_st = rows.getString(2);
            String tmp="Url : "+Integer.toString(url_index)+"";
            prg.setText(tmp);

            fw_res.write("<br><br><center><font size=5 style=\"color:red;\">"+url_index+". "+url_st+" </font></center><br><br>");

            URL url = new URL(url_st);
            String site = readUrl(url);
            fp = new File("C:\\Users\\Shashank\\IdeaProjects\\intern\\src\\website\\"+url_index+".txt");
            if(!fp.exists())
            {
                fp.createNewFile();
                FileWriter fw = new FileWriter(fp);
                String s = html2text(site);
                try
                {
                    fw.write(s);
                    fw.close();
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }

                try
                {
                    fw_res.write("<label style=\"background-color:yellow;\">"+s+" </label>");
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }
            }
            else
            {
                fr = new FileReader(fp);
                String s1 = "";
                try
                {
                    while((temp=fr.read())!=-1)
                        s1+=(char)temp;
                    fr.close();
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }

                String s2 = html2text(site);

                FileWriter fw = new FileWriter(fp);
                try
                {
                    fw.write(s2);
                    fw.close();
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }

                s1 = s1.trim().replaceAll("\\s{2,}", " ");
                s2 = s2.trim().replaceAll("\\s{2,}", " ");
                String word1[] = s1.split(" ");
                String word2[] = s2.split(" ");
                HashSet<String> diff = new HashSet<String>();
                int count = 0;
                for (int i = 0; i < word2.length; i++)
                {
                    int flag = 0;
                    for (int j = 0; j < word1.length; j++)
                    {
                        if (word2[i].equals((word1[j])))
                        {
                            flag = 1;
                            count++;
                            break;
                        }
                    }
                    if (flag == 0)
                        diff.add(word2[i]);
                }
                //fp = new File("C:\\Users\\Shashank\\IdeaProjects\\intern\\src\\Result.html");
                //fw = new FileWriter(fp);
                double perp = (word2.length-count)*100.0/word2.length;
                if(perp>=per)
                {
                    for(int i=0;i<word2.length;i++)
                    {
                        if(keyword.contains(word2[i]))
                        {
                            try
                            {
                                fw_res.write("<label style=\"background-color:#b5ffcc;\">"+word2[i]+" </label>");
                            }
                            catch (IOException e)
                            {
                                e.printStackTrace();
                            }
                        }
                        else if(diff.contains(word2[i]))
                        {
                            try
                            {
                                fw_res.write("<label style=\"background-color:yellow;\">"+word2[i]+" </label>");
                            }
                            catch (IOException e)
                            {
                                e.printStackTrace();
                            }
                        }
                        else
                        {
                            try
                            {
                                fw_res.write(word2[i]+' ');
                            }
                            catch (IOException e)
                            {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            }
        }
        fw_res.close();
        EmailSend.SendEmail("C:\\Users\\Shashank\\IdeaProjects\\intern\\src\\Result.html");
        prg.setText("Completed..!!");
    }


}
