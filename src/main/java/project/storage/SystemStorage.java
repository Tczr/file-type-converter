package project.storage;

public enum SystemStorage {
    HOME_DIR("/static/"), IMAGES_HOME("/static/images/"), TEMPLATES_HOME("/templates/"),
    SYSTEM_DIR(System.getProperty("catalina.home"));

    private String path;
    SystemStorage(String path){this.path=path;}

    public String getPath(){
        return this.path;
    }

}
