package edu.montana.AI;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import java.lang.Thread;

public class Algorithms {
    String[] colors = { "red", "green", "blue", "yellow" };
    int colors1[];

    public Algorithms() {

    }

    protected Map minConflict(Map map, String[] colors) {

        // initial assignement
        // randomAssignement(map);
        Boolean consistent = Boolean.FALSE;
        ArrayList<Connection> conflictingConnections = new ArrayList<>();
        while (!(consistent)) {
            for (Connection connection : map.connections) {
                if (!(connection.connectionCorrect())) {
                    conflictingConnections.add(connection);
                }

            }
            if (conflictingConnections.size() == 0) {
                consistent = Boolean.TRUE;
                break;
            }
            Connection conflictingConnection = conflictingConnections
                    .get((int) (Math.random() * conflictingConnections.size()));
            ArrayList<String> neighbourcolor = new ArrayList<>();
            double selectedregion = Math.random();
            if (selectedregion > 0.5) {
                neighbourcolor.add(conflictingConnection.connectedRegion1.color);// we know it to be our neighbour so we
                                                                                 // add it to the list

                for (Connection mapConnection : map.connections) {// going trought the list of connection to find the
                                                                  // one connected to the current region
                    if (mapConnection.connectedRegion1.regionId == conflictingConnection.connectedRegion2.regionId) {
                        neighbourcolor.add(mapConnection.connectedRegion2.color);
                    }
                    // change to make sure color is not added more than once
                    if (mapConnection.connectedRegion2.regionId == conflictingConnection.connectedRegion2.regionId) {
                        neighbourcolor.add(mapConnection.connectedRegion1.color);
                    }
                }

            } else {
                neighbourcolor.add(conflictingConnection.connectedRegion2.color);// we know it to be our neighbour so we
                                                                                 // add it to the list

                for (Connection mapConnection : map.connections) {// going trought the list of connection to find the
                                                                  // one connected to the current region
                    if (mapConnection.connectedRegion1.regionId == conflictingConnection.connectedRegion1.regionId) {
                        neighbourcolor.add(mapConnection.connectedRegion2.color);
                    }
                    // change to make sure color is not added more than once
                    if (mapConnection.connectedRegion2.regionId == conflictingConnection.connectedRegion1.regionId) {
                        neighbourcolor.add(mapConnection.connectedRegion1.color);
                    }
                }

            }
            // count the number of time a color is present in neighbourcolour
            int[] colorTally = new int[4];
            for (String color : neighbourcolor) {
                switch (color) {
                case "red":
                    colorTally[0]++;
                    break;
                case "green":
                    colorTally[1]++;
                    break;
                case "blue":
                    colorTally[2]++;
                    break;
                case "yellow":
                    colorTally[3]++;
                    break;
                default:
                    break;
                }
            }
            int index = 0;// index of the color the least present in the neightbour
            for (int i = 0; i < colorTally.length - 1; i++) {
                if (colorTally[i] < colorTally[index]) {
                    index = i;
                }
            }

            if (selectedregion > 0.5) {

            } else {

            }

        }

        return map;
    }

    protected Map simpleBacktracking(Map map) {
        int graph[][] = new int[map.mapSize][map.mapSize];
        graph = makeAdjacent(map, graph);
        colors1 = new int[map.mapSize];
        int numColors = 3;
        if (!(graphColoring(graph, colors1, numColors, 0))) {
            System.out.println("No 3 color");
            numColors = 4;
            graphColoring(graph, colors1, numColors, 0);
        }
        String colorName[] = new String[colors1.length];
        for (int i = 0; i < colors1.length; i++) {
            switch (colors1[i]) {
            case 1:
                colorName[i] = "red";
                break;
            case 2:
                colorName[i] = "green";
                break;
            case 3:
                colorName[i] = "blue";
                break;
            case 4:
                colorName[i] = "yellow";
                break;
            default:
                break;
            }
            map.regions[i].color = colorName[i];
        }
        return map;
    }

    protected Map backtrackingForwardChecking(Map map) {
        int graph[][] = new int[map.mapSize][map.mapSize];
        graph = makeAdjacent(map, graph);
        colors1 = new int[map.mapSize];
        int numColors = 3;
        if (!(graphColoring(graph, colors1, numColors, 0))) {
            System.out.println("No 3 color");
            numColors = 4;
            graphColoring(graph, colors1, numColors, 0);
        }
        String colorName[] = new String[colors1.length];
        for (int i = 0; i < colors1.length; i++) {
            switch (colors1[i]) {
            case 1:
                colorName[i] = "red";
                break;
            case 2:
                colorName[i] = "green";
                break;
            case 3:
                colorName[i] = "blue";
                break;
            case 4:
                colorName[i] = "yellow";
                break;
            default:
                break;
            }
            map.regions[i].color = colorName[i];
        }
        return map;
    }

    protected Map simulated_annealing(Map map, int initial_temperature, double annealing_factor, double four_color_penality) {
        Random rand = new Random();
        Boolean reached_goal = Boolean.FALSE;
        randomAssignement(map);
        int step_count = 0;
        double temperature = initial_temperature;
        while (!(reached_goal)) {
            temperature = simulated_annealing_schedule(step_count, annealing_factor, initial_temperature);
            System.out.println(temperature);
            step_count = step_count + 1;

            rand.setSeed(System.nanoTime());
            map.performance = map.goal(four_color_penality);
            System.out.println("performance " + map.performance);
            ArrayList<Connection> incorrect_connection = new ArrayList<>();
            for (Connection connection : map.connections) {
                if (!(connection.connectionCorrect())) {
                    incorrect_connection.add(connection);
                }
            }
            Connection selected_connection = incorrect_connection.get(rand.nextInt(incorrect_connection.size()));
            Map old_state = (Map) map.clone();
            String possible_colors[] = new String[3];// only 3 other possible color
            Map neighbour_states[] = new Map[3];
            int neighbour_states_index = 0;
            if (rand.nextBoolean()) {// every other time we take etheir the first connection or the second

                possible_colors=absent_color(selected_connection.connectedRegion1);
                for (String possible_color : possible_colors) {
                    selected_connection.connectedRegion1.color = possible_color;
                    map.performance = map.goal(four_color_penality);
                    neighbour_states[neighbour_states_index] = (Map) map.clone();
                    neighbour_states_index = neighbour_states_index + 1;
                }

            } else {
                // selected_connection.connectedRegion2
                possible_colors=absent_color(selected_connection.connectedRegion2);
                for (String possible_color : possible_colors) {
                    selected_connection.connectedRegion2.color = possible_color;
                    map.performance = map.goal(four_color_penality);
                    neighbour_states[neighbour_states_index] = (Map) map.clone();
                    neighbour_states_index = neighbour_states_index + 1;
                }

            }
            Map new_state = (Map) neighbour_states[0].clone();
            for (Map neighbour : neighbour_states) {
                if (new_state.performance > neighbour.performance) {
                    new_state = neighbour;
                }
            }
            if (new_state.performance > old_state.performance) {
                System.out.println("previous state better");
                double probability=Math.pow(((Double)Math.E),(-(new_state.performance/temperature)));
                if(Math.random()>probability){
                    map = (Map) new_state.clone();
                }else{
                    map = (Map) old_state.clone();
                }
            }else{
                map = (Map) new_state.clone();
            }
            
            System.out.println("performance new map " + map.performance);
            if (map.performance == 0) {
                reached_goal = Boolean.TRUE;
                break;
            }

        }

        return map;
    }

    private double simulated_annealing_schedule(int step_count, double annealing_factor, int initial_temperature) {
        return ((initial_temperature * annealing_factor) / (annealing_factor + step_count));
    }


    protected Map genetic(Map map, int population_size, int tournament_size, int number_of_parents,
            int mutation_probability, int number_of_generation_limit, double four_color_penality) {//Implementation of the genetic algorithms, with generational replacement, fixed population size and tournament selection. Implements a tentative three coloring, if the map uses four colors, it gets a penality


        Map[] population = new Map[population_size];
        Boolean reached_goal = Boolean.FALSE;
        Random rand = new Random();
        // generates the base population of population_size randomly
        for (int i = 0; i < population_size; i = i + 1) {
            population[i] = (Map) map.clone();// clone to have a separate instance of the map, or else every member of the population will be overwriting the same map
            randomAssignement(population[i]);
        }

        // start the genetic algorithm
        int generation_count = 0;//count the number of generation, is used to stop the algorithm after the maximum has been reached
        population[0].performance = population[0].goal(four_color_penality);
        Map current_best = (Map) population[0].clone();//taking the first member at the start of the algorithm. This "variable" alway holds the best individual of all generation seen so far, so that it can be returned if the maximum number of generation is reached
        while (!(reached_goal)) {//main loop of the method.
            generation_count = generation_count + 1;
            System.out.println("Currently computing generation " + generation_count);
            // evaluate all the population, keep the best individual, return if a solution
            // has been found
            Map best_of_generation = (Map) population[0].clone();//this variable holds the best individual of the current generation.
            for (Map individual : population) {//finds the best of the generation and if applicable the best of all generation.
                individual.performance = individual.goal(four_color_penality);
                if (individual.performance < current_best.performance) {
                    current_best = (Map) individual.clone();
                }
                if (individual.performance < best_of_generation.performance) {
                    best_of_generation = (Map) individual.clone();
                }
            }
            System.out.println("Current_best individual across all generation " + generation_count + " score "
                    + current_best.performance);
            System.out.println(
                    "Best individual of generation " + generation_count + " score " + best_of_generation.performance);
            if (current_best.performance == 0) {
                reached_goal = Boolean.TRUE;
                return current_best;
            }
            if (generation_count >= number_of_generation_limit) {
                System.err.println("Number of generation limit was reached, returning current best solution");
                return current_best;
            }
            Map[] parents = new Map[number_of_parents];
            for (int i = 0; i < number_of_parents; i = i + 1) {// selecting the parent via tournament_selection
                Map[] tournament_contestants = new Map[tournament_size];
                for (int j = 0; j < tournament_size; j = j + 1) {// selecting the contestant randomly within the population
                    tournament_contestants[j] = population[rand.nextInt(population_size - 1)];
                }
                // runs the tournament
                Map winner = tournament_contestants[0];
                for (Map tournament_contestant : tournament_contestants) {
                    if (tournament_contestant.performance < winner.performance) {
                        winner = tournament_contestant;
                    }
                }
                // save the winner as a parent
                parents[i] = (Map) winner.clone();
            }
            // recombine (crossover)
            for (int i = 0; i < population_size; i = i + 1) {
                population[i] = (Map) genetic_recombine(parents).clone();
            }
            // mutate
            for (Map child : population) {
                genetic_mutate(child, mutation_probability);
            }
        }
        return current_best;
    }

    private Map genetic_recombine(Map[] maps) {//this method implement uniform crossover
        Random rand = new Random();
        Map recombined = (Map) maps[0].clone();
        for (Region recombined_region : recombined.regions) {//goes trough all the region of the recombined child to color them randomly
            String[] parent_colors = new String[maps.length];
            int i = 0;
            for (Map map : maps) {//finds the color of the parents region corresponding to the currently being recombined region
                for (Region region : map.regions) {
                    if (region.regionId == recombined_region.regionId) {
                        parent_colors[i] = region.color;
                        i = i + 1;
                        break;
                    }
                }
            }
            rand.setSeed(System.nanoTime());
            recombined_region.color = parent_colors[rand.nextInt(parent_colors.length)];//select a color randomly within the parents.
        }
        return recombined;
    }

    private Map genetic_mutate(Map map, int mutation_probability) {//this method mutate the child.
        Random rand = new Random();
        for (Region region : map.regions) {//goes trough all the regions of the child
            rand.setSeed(System.nanoTime());
            if (rand.nextInt(mutation_probability) == 0) {//mutate with the probability. Only color that were not the region's color can be selected.
                String[] possible_colors=absent_color(region);
                region.color = possible_colors[rand.nextInt(3)];
            }
        }
        return map;
    }
    private String[] absent_color(Region region){//return all possible color that are not the one of the region
        String[] absent_color=new String[3];
        int absent_color_index=0;
        for (String color : colors) {
            if (!(region.color.equals(color))) {//matching colors that are not present
                absent_color[absent_color_index] = color;
                absent_color_index = absent_color_index + 1;
            }

        }
        return absent_color;
    }


    private void randomAssignement(Map map) {//color every region randomly.
        Random rand = new Random();
        rand.setSeed(System.nanoTime());
        for (Region region : map.regions) {
            region.color = colors[rand.nextInt(4)];
        }
    }

    // create adjacency matrix for map based on connected regions
    // 1 = regions are connected
    public int[][] makeAdjacent(Map map, int[][] graph) {
        for (Connection connection : map.connections) {
            int i = connection.connectedRegion1.regionId;
            int j = connection.connectedRegion2.regionId;
            graph[i][j] = 1;
            graph[j][i] = 1;
        }
        System.out.println("\n");
        return graph;
    }

    public boolean graphColoring(int[][] graph, int[] colors1, int numColors, int current) {
        if (current == colors1.length) {
            return true;
        }

        for (int i = 1; i <= numColors; i++) {
            if (assignColor(current, colors1, graph, i, numColors)) {
                colors1[current] = i;

                if (graphColoring(graph, colors1, numColors, current + 1)) {
                    return true;
                }
                colors1[current] = 0;
            }
        }
        return false;
    }
    
    public boolean assignColor(int current, int[] colors1, int[][] graph, int i, int numColors) {
        for (int j = 0; j < graph.length; j++) {
            if (graph[current][j] == 1 && i == colors1[j]) {
                return false;
            }
        }
        return true;
    }

}
