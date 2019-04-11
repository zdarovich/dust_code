package algorithms.dancers;

import java.util.AbstractMap;
import java.util.List;


public class DancerSearch {

    BinaryTree binaryTree = new BinaryTree();


    public DancerSearch() {

    }

    public AbstractMap.SimpleEntry<Dancer, Dancer> findPartnerFor(Dancer candidate) throws IllegalArgumentException {
        // TODO
        if (candidate == null) throw new IllegalArgumentException();
        //if (candidate.getHeight() <= 0 || candidate.getID() <= 0) throw new IllegalArgumentException();
        Dancer partner = null;

        if (candidate.getGender() == Dancer.Gender.MALE) {
            partner = binaryTree.findFemaleDancer(candidate.getHeight() - 4, binaryTree.root);
            if (partner != null && partner.getGender() != candidate.getGender()) {
                binaryTree.delete(partner.getHeight());
                System.out.println(partner + " with " + candidate + " made a pair.");
                return new AbstractMap.SimpleEntry<>(candidate, partner);
            }
        } else {
            partner = binaryTree.findMaleDancer(candidate.getHeight() + 4, binaryTree.root);
            if (partner != null && partner.getGender() != candidate.getGender()) {
                binaryTree.delete(partner.getHeight());
                System.out.println(candidate + " with " + partner + " made a pair.");
                return new AbstractMap.SimpleEntry<>(partner, candidate);
            }
        }

        binaryTree.addNode(candidate);


        return null;
    }

    public List<Dancer> returnWaitingList() {
        // TODO
        //return binaryTree.getDancers(binaryTree.root);
        List<Dancer> dancers = binaryTree.getDancerListAscending();
        for (int i = 1; i < dancers.size(); i++) {
            Dancer previous = dancers.get(i - 1);
            Dancer current = dancers.get(i);
            if (current.getGender() == Dancer.Gender.FEMALE
                    && previous.getHeight() == current.getHeight()
                    && previous.getGender() == Dancer.Gender.MALE) {
                dancers.remove(i - 1);
                dancers.add(i - 1, current);
                dancers.remove(i);
                dancers.add(i, previous);
            }
        }
        return dancers;
    }

}
