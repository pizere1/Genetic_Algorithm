import java.util.ArrayList;
import java.util.Random;

public class Individual {

    /**
     * Chromosome stores the individual's genetic data as an ArrayList of characters
     * Each character represents a gene
     */
    /**
     * By default, the length of our chromosome,unles otherwise stated will be 8.
     */
    ArrayList<Character> chromosome=new ArrayList<>();
    int c_max=8;

    /**
     * Initial constructor to generate initial population members
     * @param c_0 The initial chromosome size
     * @param num_letters The number of letters available to choose from
     */
    public Individual(int c_0, int num_letters, Random rng) {
        for(int i=0; i<c_0; i++) {
            Character gene=randomLetter(num_letters,rng);
            chromosome.add(gene);
        }
    }
    /**
     * Second constructor to create parents and offspring chromosomes
     * @param parent1 The first parent chromosome
     * @param parent2 The second parent chromosome
     * @param c_max The maximum chromosome size
     * @param num_letters number of letters available to choose from??
     * @param m The chances per round of mutation in each gene
     * @param rng a random value generator
     */
    public Individual(Individual parent1, Individual parent2, int c_max, float m, int num_letters, Random rng) {
        this.c_max=c_max;
        ArrayList<Character> prefix=new ArrayList<>();
        ArrayList<Character> suffix=new ArrayList<>(rng.nextInt(parent2.chromosome.size()));
        int prefixlength=rng.nextInt(parent1.chromosome.size());
        int suffixlength=rng.nextInt(parent2.chromosome.size());
        for(int i=0; i<prefixlength;i++){
            prefix.add(parent1.chromosome.get(i));
        }
        for(int i=0; i<suffixlength;i++){
            suffix.add(0, parent2.chromosome.get(parent2.chromosome.size()-i-1));//reading from the back
        }
        chromosome=prefix;
        for(Character gene:suffix){
            chromosome.add(gene);
        }
       // System.out.println(chromosome.toString());
        //System.out.println(chromosome.size());
        if(chromosome.size()>c_max){
           /** for(int i=c_max; i<chromosome.size();i++){
                chromosome.remove(c_max);
            }**/
            for(int i=c_max; i<=chromosome.size(); i++){
                chromosome.removeLast();
            }
        }
       for(int i=0; i<chromosome.size();i++){
            Character gene;
            if(this.doesMutate(m,rng)){
                gene=randomLetter(num_letters,rng);
                chromosome.set(i,gene);//Generate a new gene to replace the old
            }
        }
    }
    public int getFitness(){
        int fitness=0;
        int half_limit=chromosome.size()/2;
        if(chromosome.size()%2!=0){
            fitness+=1;
        }else{
            fitness+=0;
        }
        for(int i=0; i<half_limit;i++){
            if(chromosome.get(i).equals(chromosome.get(chromosome.size()-1-i))){
                fitness++;
            }else{//if they're not equal
                fitness--;
            }
        }
        for(int i=0; i<chromosome.size()-1;i++){
            if(chromosome.get(i).equals(chromosome.get(i+1))){
                fitness--;
            }
        }

        return fitness;
    }

    /**
     * Provided method to choose a letter at random, in the range from A to the number of states indicated
     * @param num_letters The number of letters available to choose from (number of possible states)
     * @param rng The random number generator being used for the current run
     * @return The letter as a Character
     */
    private Character randomLetter(int num_letters, Random rng) {
        return Character.valueOf((char)(65 + rng.nextInt(num_letters)));
    }

    /**
     * Provided method to determine whether a given gene will mutate based on the parameter ***m***
     * @param m the mutation rate
     * @param rng The random number generator being used for the current run
     * @return true if a number randomly chosen between 0 and 1 is less than ***m***, else false
     */
    private Boolean doesMutate(float m, Random rng) {
        float randomNum = rng.nextInt(100) / 100f;
        return randomNum < m;
    }

    /**
     * Expresses the individual's chromosome as a String, for display purposes
     * @return The chromosome as a String
     */
    public String toString() {
        StringBuilder builder = new StringBuilder(chromosome.size());
        for(Character ch: chromosome) {
            builder.append(ch);
        }
        return builder.toString();
    }




    public static void main(String[] args) {
        // This code will set a random seed when you're testing Individual (i.e., running it without GA_Simulation)
        Random rng = new Random(System.currentTimeMillis());

        // You can pass rng, as defined above, to your constructors.
        Individual i = new Individual(8, 26, rng);
        Individual j = new Individual(8, 26, rng);
        System.out.println(i);
        System.out.println(j);
        Individual k = new Individual(i,j,8, 9.0f, 6, rng);
        System.out.println(k);
        System.out.println(k.getFitness());

    }

}
