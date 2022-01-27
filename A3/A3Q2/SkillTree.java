package lib280.tree;

import lib280.list.LinkedList280;
import lib280.tree.BasicMAryTree280;

public class SkillTree extends BasicMAryTree280<Skill> {

    /**
     * Create lib280.tree with the specified root node and
     * specified maximum arity of nodes.
     * @timing O(1)
     * @param x item to set as the root node
     * @param m number of children allowed for future nodes
     */
    public SkillTree(Skill x, int m)
    {
        super(x,m);
    }

    /**
     * A convenience method that avoids typecasts.
     * Obtains a subtree of the root.
     *
     * @param i Index of the desired subtree of the root.
     * @return the i-th subtree of the root.
     */
    public SkillTree rootSubTree(int i) {
        return (SkillTree)super.rootSubtree(i);
    }

    /**
     * Computes number of skill points to unlock skill
     *
     * @param list of skill whose skill points are to be computed
     * @return total number of skill points to unlock skill
     * @throw ItemNotFound280Exception if skill not found in tree
     */
    public int skillTotalCost(Skill skillName){
        int totalCost = 0;
        LinkedList280 list = new LinkedList280();
        list = this.skillDependencies(skillName);
        //list.goFirst();
        while(!list.after()){
            list.goForth();
            System.out.println(list.item());
            //totalCost += list.item()
        }
        return totalCost;
    }


    /**
     * helper method of skillDependencies() that traverses the tree recursively to find skill
     *
     * @param root representing current node being searched, skillName, list
     * @return true if needs to iterate again false otherwise
     */
    protected boolean skillDependenciesHelper( MAryNode280<Skill> root , Skill skillName, LinkedList280<Skill> list) {
        if(this.isEmpty()) return false;
        else{
            for (int i = 1; i <= root.lastNonEmptyChild(); i++) {
                if (root.item() != null) {
                    if (!list.isEmpty()) {    //if skill was found
                        list.insert(root.item());
                        return true;

                    }

                    if (root.item().getSkillName().equals(skillName.getSkillName())) {    //if skill was found
                        list.insert(root.item());}
                    else skillDependenciesHelper(root.subnode(i), skillName, list);

                }


            }
        }
        return false;

    }


    /**
     * public method of skillDependencies() that creates list of prerequisites needed for a skill to be unlocked
     *
     * @param skillName of skill whose skill prerequisites are to be printed out
     */
    public LinkedList280 skillDependencies(Skill skillName) {
        //initializes linkedlist to be passed to protected
        LinkedList280<Skill> skillList = new LinkedList280<Skill>();

        if (!this.isEmpty())
            skillDependenciesHelper(this.rootNode, skillName, skillList);
            System.out.println(skillList);
            return skillList;



    }

    public static void main(String[] args){

        Skill goalSetting = new Skill("Goal Setting", "temp", 1);
        Skill budgeting = new Skill("Budgeting", "temp", 1);
        Skill communication = new Skill("Communication", "temp", 2);
        Skill creativity = new Skill("Creativity", "temp", 4);

        Skill ideation = new Skill("Ideation", "temp", 2);
        Skill analyticalThinking = new Skill("Analytical Thinking", "temp", 2);
        Skill sales = new Skill("Sales", "temp", 2);
        Skill partnerships = new Skill("Partnerships", "temp", 3);
        Skill fundraising = new Skill("Fundraising", "temp", 5);
        Skill strategy = new Skill("Strategy", "temp", 2);
        Skill projMngmt = new Skill("Project Management", "temp", 2);


        SkillTree founder = new SkillTree(goalSetting, 3);
        founder.setRootSubtree(new SkillTree(budgeting,3),1);
        founder.setRootSubtree(new SkillTree(communication,3),2);
        founder.setRootSubtree(new SkillTree(creativity,3),3);

        SkillTree Budgeting = founder.rootSubTree(1);
        Budgeting.setRootSubtree(new SkillTree(analyticalThinking,1),1);

        SkillTree AnalyticalThink = Budgeting.rootSubTree(1);
        AnalyticalThink.setRootSubtree(new SkillTree(strategy, 1),1);

        SkillTree Strategy = AnalyticalThink.rootSubTree(1);
        Strategy.setRootSubtree(new SkillTree(projMngmt, 1),1);

        SkillTree Communication = founder.rootSubTree(2);
        Communication.setRootSubtree(new SkillTree(sales,1),1);
        Communication.setRootSubtree(new SkillTree(partnerships,1),2);

        SkillTree Partnership = Communication.rootSubTree(2);
        Partnership.setRootSubtree(new SkillTree(fundraising, 1),1);

        SkillTree Creativity = founder.rootSubTree(3);
        Creativity.setRootSubtree(new SkillTree(ideation,1),1);

        System.out.println(founder.toStringByLevel());
        System.out.println("");
        System.out.println("Dependencies for " + communication.getSkillName());
        founder.skillDependencies(communication);
        System.out.println("");
        System.out.println("Dependencies for " + partnerships.getSkillName());
        founder.skillDependencies(partnerships);
        System.out.println("");
        System.out.println("Dependencies for " + creativity.getSkillName());
        founder.skillDependencies(creativity);
        System.out.println("");
        System.out.println("Dependencies for " + strategy.getSkillName());
        founder.skillDependencies(strategy);
        System.out.println("");
        System.out.println("To get "+ sales.getSkillName() + " you must invest ");
        System.out.println(founder.skillTotalCost(sales));

    }


}
