import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

public class Main 
{
    public static Scanner in;
    public static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
    public static final String dataPath = "C:\\temp\\";

    public static void main(String[] args) 
    {        
        in = new Scanner(System.in, "Cp866");
        String inputData = "";
        boolean isValidData = false;
        
        try
        {
            inputData = DataInput();
            if (!inputData.isEmpty())
            {
                System.out.println("Введённые данные: " + inputData);
                isValidData = true;
            }
        }
        catch (Exception ex)
        {
            System.out.println(ex.getMessage());
        }

        if (isValidData)
        {
            try
            {
                System.out.println("ФИО: " + ParseFIO(inputData));
            }
            catch (Exception ex)
            {
                System.out.println(ex.getMessage());
            }
        }
     
        if (isValidData)
        {
            try
            {
                System.out.println("Пол: " + ParseGender(inputData));
            }
            catch (Exception ex)
            {
                System.out.println(ex.getMessage());
            }
        }

        if (isValidData)
        {
            try
            {
                System.out.println("Телефон: " + ParsePhone(inputData));
            }
            catch (Exception ex)
            {
                System.out.println(ex.getMessage());
            }
        }

        if (isValidData)
        {
            try
            {
                System.out.println("Дата рождения: " + ParseBirthDate(inputData).format(formatter));
            }
            catch (Exception ex)
            {
                System.out.println(ex.getMessage());
            }
        }

        if (isValidData)
        {            
            try 
            {
                SaveToFile(inputData);
            } 
            catch (Exception e) 
            {
                e.printStackTrace();
                System.out.println(e.getMessage());
            }
        }
    }

    public static String DataInput() throws Exception
    {
        String result;
        System.out.println("Введите следующие данные через пробел: Фамилия Имя Отчество, дата рождения, номер телефона, пол. Для выхода нажмите enter.");        
        result = in.nextLine();
        if (result.isEmpty())
        {
            return "";
        }
        else if (result.split(" ").length < 6)
        {
            throw new Exception("Вы ввели меньше данных, чем требуется");
        }
        else if (result.split(" ").length > 6)
        {
            throw new Exception("Вы ввели больше данных, чем требуется");
        }
        else
        {
            return result;
        }
    }

    public static String ParseGender(String inputData) throws Exception
    {
        String gender = "";
        String[] arr = inputData.split(" ");

        for (int i = 0; i < arr.length; i++)
        {
            if (arr[i].equals("f") || arr[i].equals("m"))
            {
                gender = arr[i];
                break;
            }
        
        }
        
        if (gender.isEmpty())
        {
            throw new Exception("Вы не ввели пол человека");
        }

        return gender;
    }
       
    public static long ParsePhone(String inputData) throws Exception
    {
        long phone = 0;
        String[] arr = inputData.split(" ");

        for (int i = 0; i < arr.length; i++)
        {
            try
            {
                phone = Long.parseLong(arr[i]);
                break;
            }
            catch (Exception e)
            {
                phone = 0;
            }
        }
        
        if (phone == 0)
        {
            throw new Exception("Вы не ввели телефон");
        }

        return phone;
    }

    public static LocalDate ParseBirthDate(String inputData) throws Exception
    {
        LocalDate dateTime = null;
        String[] arr = inputData.split(" ");
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");

        for (int i = 0; i < arr.length; i++)
        {
            try 
            {
                dateTime = LocalDate.parse(arr[i], formatter); 
                break;
            } 
            catch (Exception e) 
            {
                dateTime = null;
            }
        }
        
        if (dateTime == null)
        {
            throw new Exception("Вы не ввели дату рождения");
        }

        return dateTime;
    
    }

    public static String ParseFIO(String inputData) throws Exception
    {
        String fio = "";
        String[] arr = inputData.split(" ");

        for (int i = 0; i <= 3; i++)
        {
            if (!arr[i].equals("f") && !arr[i].equals("m") && !IsContainsDigit(arr[i]))
            {
                fio = arr[i] + " " + arr[i+1] + " " + arr[i+2];
                break;
            }
        }

        if (fio.isEmpty())
        {
            throw new Exception("Вы не ввели ФИО");
        }

        return fio;
    }

    public static boolean IsContainsDigit(String str)
    {
        return str.matches(".*\\d.*");
    }

    public static String MakeString(String inputData) throws Exception 
    {
        StringBuilder sb = new StringBuilder();
        sb
        .append("<").append(ParseFIO(inputData).split(" ")[0]).append(">")
        .append("<").append(ParseFIO(inputData).split(" ")[1]).append(">")
        .append("<").append(ParseFIO(inputData).split(" ")[2]).append("> ")
        .append("<").append(ParseBirthDate(inputData).format(formatter)).append(">")
        .append("<").append(ParsePhone(inputData)).append(">")
        .append("<").append(ParseGender(inputData).toString()).append(">");
        
        return sb.toString();
    }
    
    public static String getFirstName(String inputData)  
    {
        try 
        {
            return ParseFIO(inputData).split(" ")[0];
        } 
        catch (Exception e) 
        {
            System.out.println(e.getMessage());
        }
        return "";
    }

    private static void SaveToFile(String inputData) throws Exception 
    {
        String filePath = dataPath + getFirstName(inputData) + ".txt";
        if (!CheckFileName(filePath))
        {
            File file = new File(filePath);
            file.createNewFile();

            try (FileWriter fw = new FileWriter(file, Charset.forName("UTF-8")))
            {
                fw.write(MakeString(inputData) + "\n");
            } 
            catch (IOException e) 
            {
                e.printStackTrace();
            }
            catch (Exception e)
            {
                throw new Exception("Ошибка при попытке сохранить данные...");
            }
        }
        else
        {
            File file = new File(filePath);
            try (FileWriter fw = new FileWriter(file, Charset.forName("UTF-8"), true))
            {
                fw.append(MakeString(inputData) + "\n");
            }
            catch (IOException e) 
            {
                e.printStackTrace();
            }
            catch (Exception e) 
            {
                throw new Exception("Ошибка при попытке сохранить данные...");
            }            
        }
    }

    private static boolean CheckFileName(String filePath)
    {
        File file = new File(filePath); 
        return file.exists();
    }
}