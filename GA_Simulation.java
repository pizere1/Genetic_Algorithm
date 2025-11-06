import java.util.ArrayList;
import java.util.Comparator;
import java.util.Random;

public class GA_Simulation {

    /**
     * Simulation Class to simulate the evolution
     */
    public static Random rng;
    int n, k, r, c_0, c_max, g;
    float m;
    ArrayList <Individual> generation1=new ArrayList<>();
    /**
     * Constructor for  thr GA_simulator class
     * @param n takes in the number of individuals in each generation
     * @param k takes in the number of winners (individuals allowed to reproduce) in each generation
     * @param r takes in the number of rounds of evolution to run
     * @param c_0  takes in the initial chromosome size of the chromosome
     * @param c_max takes in the maximum chromosome size
     * @param m takes the Chance per round of a mutation in each gene
     * @param g holds the number of states possible per gene
     */

    GA_Simulation(int n, int k, int r, int c_0, int c_max, float m, int g){
        this.n = n;
        this.k = k;
        this.r = r;
        this.c_max = c_max;
        this.m = m;
        this.g = g;
    }

    /**
     *  Function to initialize the population
     * @return an arraylist of the generation
     */
    public ArrayList<Individual> init(){
        rng = new Random();
        for(int i=0; i<n; i++){
            Individual individual=new Individual(c_max, g, rng);
            generation1.add(individual);
        }
        return generation1;
    }

    /**
     *  Provided method that sorts population by fitness score, best first
     * @param pop: ArrayList of Individuals in the current generation
     */
    public void rankPopulation(ArrayList<Individual> pop) {
        // sort population by fitness
        Comparator<Individual> ranker = new Comparator<>() {
            // this order will sort higher scores at the front
            public int compare(Individual c1, Individual c2) {
                return (int)Math.signum(c2.getFitness()-c1.getFitness());
            }
        };
        pop.sort(ranker);
    }
    /**
     * Function to select the top k individuals and create n offspirings
     * @param generation takes in the current parent generation
     * @param k takes in the number of top individuals to select from the generation
     * @param n takes in the number of offsprings to create
     * @return an arraylist of a new generation of offsprings
     */
    public ArrayList<Individual> evolve(ArrayList<Individual> generation, int k, int n){
        rankPopulation(generation);
       // rng = new Random();
        ArrayList<Individual> best=new ArrayList<>();
        ArrayList<Individual> new_generation =new ArrayList<>();
        for(int i=0; i<k; i++){
            best.add(generation.get(i));
        }
        for (int i=0; i<n; i++){
            Individual parent1=best.get(rng.nextInt(best.size()));//random parent from the best
            Individual parent2=best.get(rng.nextInt(best.size()));//random parent from the best
            Individual offspring= new Individual(parent1, parent2, c_max,m, g,rng);
            new_generation.add(offspring);
        }
        return new_generation;
    }
    /** Provided method that prints out summary statistics for a given
     * generation, based on the information provided
     * @param roundNumber: Which round of evolution are we on, from 0 to n
     * @param bestFitness: Fitness of top-ranked (most fit) individual
     * @param kthFitness: Fitness of kth-ranked individual
     * @param leastFitness: Fitness of lowest-ranked (least fit) individual
     * @param best: Individual with highest fitness
     */
    private void printGenInfo(int roundNumber, int bestFitness, int kthFitness, int leastFitness, Individual best) {
        System.out.println("Round " + roundNumber + ":");
        System.out.println("Best fitness: " + bestFitness);
        System.out.println("k-th (" + k + ") fitness: " + kthFitness);
        System.out.println("Least fit: " + leastFitness);
        System.out.println("Best chromosome: " + best);
        System.out.println(); // blank line to match the example format
    }

    /** Provided method that prints out summary statistics for a given
     * generation, based on the information provided
     * @param roundNumber: Which round of evolution are we on, from 0 to n
     * @param bestFitness: Fitness of top-ranked (most fit) individual
     * @param kthFitness: Fitness of kth-ranked individual
     * @param leastFitness: Fitness of lowest-ranked (least fit) individual
     * @param best: Individual with highest fitness
     */

    public void describeGeneration(int roundNumber, int bestFitness, int kthFitness, int leastFitness, Individual best){
        printGenInfo(roundNumber, bestFitness, kthFitness, leastFitness,best);
    }

    /**
     * Function to run the simulation
     */
    public void run(){
        ArrayList<Individual> population;
       // rng = new Random();
        ArrayList<ArrayList<Individual>> generations=new ArrayList<>();
        int roundNumber;
        population=init();
        rankPopulation(population);
        Individual bestfit=population.get(0);
        Individual kth= population.get(rng.nextInt(population.size()));
        Individual leastfit=population.get(population.size()-1);
        printGenInfo(0, bestfit.getFitness(),  kth.getFitness(), leastfit.getFitness(), bestfit);
        generations.add(population);
        for(roundNumber=1;roundNumber<=r;roundNumber++){
            ArrayList<Individual> new_generation=evolve(generations.get(roundNumber-1), k, n);//create a new generation by evolving the previous generation
            rankPopulation(new_generation);
            describeGeneration(roundNumber, new_generation.get(0).getFitness(),
                    new_generation.get(rng.nextInt(new_generation.size())).getFitness(),
                    new_generation.get(new_generation.size()-1).getFitness(), new_generation.get(0));
            generations.add(new_generation);//adding the new generation to the old one
        }
    }

    /**
     * Main method
     * @param args takes in string arguments
     */
    public static void main(String[] args) {

        long seed = System.currentTimeMillis(); // default
        if (args.length > 0) {
            try {
                seed = Long.parseLong(args[0]);
            } catch (NumberFormatException e) {
                System.err.println("Seed wasn't passed so using current time.");
            }
        }
        rng = new Random(seed);

    }

}
