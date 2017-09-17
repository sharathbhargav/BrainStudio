package brainstudio.s4pl.com.brainstudio;

/**
 * Created by SharathBhargav on 17-09-2017.
 */

public class programmeData {

   public String cubeInfo,cubeBenifits,jugglingInfo,jugglingBenifits,graphoInfo,graphoBenifits,stackInfo,stackBenifits,
            calligraphyInfo,calligraphyBenifits,corporateInfo,corporateBenifits;



    public String cubeHead,jugglingHead,graphoHead,stackHead,calligraphyHead,corporateHead;
   public String getInfo(String param)
    {
        switch (param)
        {
            case "cube":
                return cubeInfo;
            case "juggling":
                return jugglingInfo;
            case "grapho":
                return graphoInfo;
            case "stack":
                return stackInfo;
            case "calligraphy":
                return calligraphyInfo;
            case "corporate":
                return corporateInfo;
            default:
                return "Error";
        }
    }

   public String getBenifits(String param)
    {
        switch (param)
        {
            case "cube":
                return cubeBenifits;
            case "juggling":
                return jugglingBenifits;
            case "grapho":
                return graphoBenifits;
            case "stack":
                return stackBenifits;
            case "calligraphy":
                return calligraphyBenifits;
            case "corporate":
                return corporateBenifits;
            default:
                return "Error";
        }
    }


    public String getHead(String param)
    {
        switch (param)
        {
            case "cube":
                return cubeHead;
            case "juggling":
                return jugglingHead;
            case "grapho":
                return graphoHead;
            case "stack":
                return stackHead;
            case "calligraphy":
                return calligraphyHead;
            case "corporate":
                return corporateHead;
            default:
                return "Error";
        }
    }

  public   void setData(String info,String benifits,String name,String param)
    {
        switch (param)
        {
            case "cube":
                cubeInfo=info;
                cubeBenifits=benifits;
                cubeHead=name;
                break;
            case "juggling":
                jugglingInfo=info;
                jugglingBenifits=benifits;
                jugglingHead=name;
                break;
            case "grapho":
                graphoBenifits=benifits;
                graphoInfo=info;
                graphoHead=name;
                break;
            case "stack":
                stackBenifits=benifits;
                stackInfo=info;
                stackHead=name;
                break;
            case "calligraphy":
                calligraphyBenifits=benifits;
                calligraphyInfo=info;
                calligraphyHead=name;

                break;
            case "corporate":
                corporateBenifits=benifits;
                corporateInfo=info;
                corporateHead=name;
                break;

        }
    }


}
