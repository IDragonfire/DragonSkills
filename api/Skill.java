package api;

public abstract class Skill {
    public String getSkillName() {
        return this.getClass().getName();
    }
}
