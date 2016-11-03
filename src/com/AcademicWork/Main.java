package com.AcademicWork;

import java.util.*;

public class Main {

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);
        String inputTaxonomy = scanner.nextLine();
        long startTime = System.currentTimeMillis();

        String[] taxonomy = inputTaxonomy.split("\\s+");

        int numberOfPersons = Integer.parseInt(taxonomy[0]);
        int numberOfRelations = Integer.parseInt(taxonomy[1]);

        List<Person> persons = new ArrayList<>();
        List<Relation> relations = new ArrayList<>();

        for (int i = 0; i < numberOfPersons; i++) {
            persons.add(new Person(i));
        }

        for (int i = 0; i < numberOfPersons; i++) {
            int balance = scanner.nextInt();
            scanner.nextLine();
            persons.get(i).setBalance(balance);
        }

        for (int i = 0; i < numberOfRelations; i++) {
            String inputRelations = scanner.nextLine();
            String[] friendChain = inputRelations.split("\\s+");

            int idPersonOne = Integer.parseInt(friendChain[0]);
            int idPersonTwo = Integer.parseInt(friendChain[1]);
            persons.get(idPersonOne).hasFriend = true;
            persons.get(idPersonTwo).hasFriend = true;
            relations.add(new Relation(persons.get(idPersonOne), persons.get(idPersonTwo)));
        }
        long stopTime = System.currentTimeMillis();
        System.out.println("Users and relations done: " + (System.currentTimeMillis() - startTime));

        // init the list of relation chains
        List<RelationChain> listOfRelationChains = new ArrayList<>();
        Map<Person, RelationChain> personAndChain = new HashMap<>();

        for (int i = 0; i < relations.size(); i++) {
            // kolla mot alla kedjor
            Person personOne = relations.get(i).personOne;
            Person personTwo = relations.get(i).personTwo;

            if (personAndChain.containsKey(personOne) &&
                    personAndChain.containsKey(personTwo) &&
                    personAndChain.get(personOne) != personAndChain.get(personTwo)) {
                for (Person person :
                        personAndChain.get(personTwo).getChainOfPersons()) {
                    personAndChain.get(personOne).addToChainOfPersons(person);
                }
                listOfRelationChains.remove(personAndChain.get(personTwo));
            }

            if (personAndChain.containsKey(personOne) || personAndChain.containsKey(personTwo)) {
                if (!personAndChain.containsKey(personOne)) {
                    personAndChain.get(personTwo).addToChainOfPersons(personOne);
                    personAndChain.put(personOne, personAndChain.get(personTwo));
                }
                if (!personAndChain.containsKey(personTwo)) {
                    personAndChain.get(personOne).addToChainOfPersons(personTwo);
                    personAndChain.put(personTwo, personAndChain.get(personOne));
                }
            } else {

                RelationChain newRelatonChain = new RelationChain();
                newRelatonChain.addToChainOfPersons(personOne);
                personAndChain.put(personOne, newRelatonChain);

                newRelatonChain.addToChainOfPersons(personTwo);
                personAndChain.put(personTwo, newRelatonChain);
                listOfRelationChains.add(newRelatonChain);
            }
        }
        System.out.println("Relationchain done: " + (System.currentTimeMillis() - startTime));
        // sum all balances in relation chains
        for (RelationChain relationChain :
                listOfRelationChains) {
            for (Person person :
                    relationChain.getChainOfPersons()) {
                relationChain.addToSum(person.getBalance());

            }
        }

        // see if all balances are 0
        String possible = "POSSIBLE";
        for (RelationChain relationChain :
                listOfRelationChains) {
            if (relationChain.getTotalBalance() != 0) {
                possible = "IMPOSSIBLE";
            }
        }
        for (Person person :
                persons) {
            if (person.hasFriend == false) {
                if (person.getBalance() != 0) {
                    possible = "IMPOSSIBLE";
                    break;
                }
            }
        }

        System.out.println(possible);
        System.out.println("Program done: " + (System.currentTimeMillis() - startTime));
    }
}

class Person {
    int id;
    int balance;
    boolean hasFriend = false;

    public Person(int id) {
        this.id = id;
        this.balance = 0;
    }

    public void setBalance(int balance) {
        this.balance = balance;
    }

    public int getBalance() {
        return balance;
    }

    public void addBalance(int balance) {
        this.balance += balance;
    }
}

class Relation {
    Person personOne;
    Person personTwo;

    public Relation(Person personOne, Person personTwo) {
        this.personOne = personOne;
        this.personTwo = personTwo;
    }
}

class RelationChain {

    private List<Person> chainOfPersons = new ArrayList<>();
    private int totalBalance = 0;

    public List<Person> getChainOfPersons() {

        return chainOfPersons;
    }

    public void addToChainOfPersons(Person person) {
        chainOfPersons.add(person);
    }

    public boolean listContains(Person personToEvaluate) {

        if (chainOfPersons.isEmpty()) {
            return false;
        }
        for (Person person :
                chainOfPersons) {
            if (person == personToEvaluate) {
                return true;
            }
        }
        return false;
    }

    public void addToSum(int balance) {
        totalBalance += balance;
    }

    public int getTotalBalance() {
        return totalBalance;
    }
}