public class Context {

    public static Config config;

    public Context(){
        if(config==null){
            config=new Config();
        }
    }
}
