import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Chromosome {
    private final List<Integer> genes;

    public Chromosome(int size) {
        List<Integer> genes = new ArrayList<>(size);
        for (int i = 0; i < size; i++) {
            genes.add(i);
        }
        Collections.shuffle(genes);
        this.genes = Collections.unmodifiableList(genes);
    }

    private Chromosome(List<Integer> genes) {
        this.genes = Collections.unmodifiableList(genes);
    }

    public int size() {
        return genes.size();
    }

    public int get(int index) {
        return genes.get(index);
    }

    public Chromosome crossover(Chromosome other, int from, int to) {
        List<Integer> genes = new ArrayList<>(this.genes.size());
        genes.addAll(this.genes.subList(from, to));
        for (int i = 0; i < other.genes.size(); i++) {
            int gene = other.genes.get((to + i) % other.genes.size());
            if (!genes.contains(gene)) {
                genes.add(gene);
            }
        }
        Collections.rotate(genes, from);
        return new Chromosome(genes);
    }

    public Chromosome mutate(int i, int j) {
        List<Integer> genes = new ArrayList<>(this.genes);
        Collections.swap(genes, i, j);
        return new Chromosome(genes);
    }

    public int[] toArray() {
        return genes.stream().mapToInt(Integer::intValue).toArray();
    }

    @Override
    public String toString() {
        return "Chromosome{" +
                "genes=" + genes +
                '}';
    }
}
