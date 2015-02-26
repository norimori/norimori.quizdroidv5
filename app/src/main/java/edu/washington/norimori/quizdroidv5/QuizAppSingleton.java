package edu.washington.norimori.quizdroidv5;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by midori on 2/23/15.
 * Singleton object form of QuizApp.
 */
public class QuizAppSingleton implements TopicRepository {

    private static QuizAppSingleton instance; //Singleton instance of QuizApp
    private List<Topic> allTopics; //Collection of all available quiz topics

    //Initialize instance.
    //Allows internal instance to be initialized only once, which is done in QuizApp class.
    public static void initInstance() {
        if (instance == null) {
            instance = new QuizAppSingleton();
        } else {
            throw new RuntimeException("You cannot instantiate more than one QuizAppSingleton");
        }
    }

    //Get instance from any part of application.
    public static QuizAppSingleton getInstance() {
        return instance;
    }

    //Constructor is private because this is a singleton (won't allow creation of multiple instances).
    private QuizAppSingleton() {}

    //Returns index of given topic in topic list.
    public int getTopicIndex(String topicName) {
        for (int i = 0; i < allTopics.size(); i++) {
            if (allTopics.get(i).getName().equalsIgnoreCase(topicName)) {
                return i;
            }
        }
        return -1; //Did not find topic in topic list.
    }

    /**
     * Methods from TopicRepository interface.
     */
    //Returns topic object of given topic name.
    public Topic getTopic(String topicName) {
        if(getTopicIndex(topicName) != -1) {
            return allTopics.get(getTopicIndex(topicName));
        }
        return null;
    }

    //Adds topic to topic list.
    public void addTopic(Topic newTopic) {
        allTopics.add(newTopic);
    }

    //Updates current topic in list with given topic. (Must have same name).
    public void updateTopic(Topic topic) {
        if(getTopicIndex(topic.getName()) != -1) {
            allTopics.set(getTopicIndex(topic.getName()), topic);
        }
    }

    //Removes topic from topic list.
    public void deleteTopic(Topic topic) {
        if(getTopicIndex(topic.getName()) != -1) {
            allTopics.remove(topic);
        }
    }

    //Hardcoding all data for MainActivity's list: public String[] topics = {"Math", "Physics", "Marvel Super Heroes", "Pokemon"};
    //Returns list of all topics.
    public List<Topic> getEverything() {
        allTopics = new ArrayList<Topic>();

        //Create all Topics and Question objects
        Quiz pokemonQ1 = new Quiz("What type is Pikachu?", "Electric", "Psychic", "Water", "Fire", 1 );
        Quiz pokemonQ2 = new Quiz("What type is Jigglypuff?", "Steel", "Normal", "Water", "Bug", 2 );
        Quiz pokemonQ3 = new Quiz("What type is Glaceon?", "Leaf", "Dark", "Ghost", "Ice", 4 );
        List<Quiz> pokemonQuizzes = new ArrayList<Quiz>();
        pokemonQuizzes.add(pokemonQ1);
        pokemonQuizzes.add(pokemonQ2);
        pokemonQuizzes.add(pokemonQ3);
        Topic pokemon = new Topic("Pokemon", "Gotta catch'em all!", "Pokémon (Pokemon is a media franchise owned by The Pokémon Company, and created by Satoshi Tajiri in 1995. It is centered on fictional creatures called \"Pokémon\", which humans capture and train to fight each other for sport.",
                pokemonQuizzes, 3);
        allTopics.add(pokemon);

        Quiz mathQ1 = new Quiz("1 + 1 = ?", "5", "1", "3", "2", 4);
        Quiz mathQ2 = new Quiz("9000 x 0 = ?", "0", "1", "9000", "90000", 1);
        Quiz mathQ3 = new Quiz("5 + 5 = ?", "0", "55", "10", "-10", 3);
        Quiz mathQ4 = new Quiz("10 - 7 = ?", "-3", "107", "3", "17", 3);
        Quiz mathQ5 = new Quiz("7 + 5", "21", "12", "75", "-57", 2);
        List<Quiz> mathQuizzes = new ArrayList<Quiz>();
        mathQuizzes.add(mathQ1);
        mathQuizzes.add(mathQ2);
        mathQuizzes.add(mathQ3);
        mathQuizzes.add(mathQ4);
        mathQuizzes.add(mathQ5);
        Topic math = new Topic("Math", "MAAAAAAAATH", "MAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAATH",
                mathQuizzes, 5);
        allTopics.add(math);

        Quiz physicsQ1 = new Quiz("What famous dude had an apple fall on his head?", "Charmander", "Mikasa", "Jeffery", "Isaac Newton", 4);
        Quiz physicsQ2 = new Quiz("When was Albert Einsten Born?", "March 14th, 1879", "Today", "Christmas", "Next year", 1);
        Quiz physicsQ3 = new Quiz("How much is gravity on Earth?", "FALSE", "NO", "9.807m/s^2", "yes", 3);
        Quiz physicsQ4 = new Quiz("How do you calculate speed?", "atmosphereic pressure/weight", "density/volume", "distance/time", "your enthusiasm!", 3);
        Quiz physicsQ5 = new Quiz("What is the density of water?", "PIE", "999.97 kg/m^3", "-5", "0", 2);
        Quiz physicsQ6 = new Quiz("What is the boiling point of water?", "-50F", "100C", "infinty", "never", 2);
        List<Quiz> physicsQuizzes = new ArrayList<Quiz>();
        physicsQuizzes.add(physicsQ1);
        physicsQuizzes.add(physicsQ2);
        physicsQuizzes.add(physicsQ3);
        physicsQuizzes.add(physicsQ4);
        physicsQuizzes.add(physicsQ5);
        physicsQuizzes.add(physicsQ6);
        Topic physics = new Topic("Physics", "IT'S EVERYWHERE ∩(ﾟ∀ﾟ∩)", "The effort to formulate phenomena mathematically in the hope of extending one's range of mastery over them.",
                physicsQuizzes, 6);
        allTopics.add(physics);

        Quiz marvelSuperHeroesQ1 = new Quiz("Who is the strongest Marvel super hero", "Archer", "Ironman", "Hulk", "Spiderman", 3);
        Quiz marvelSuperHeroesQ2 = new Quiz("Who was not in the X-Men originally?", "Wolverine", "Professor X", "Iceman", "Beast", 1);
        Quiz marvelSuperHeroesQ3 = new Quiz("What is Professor X's full name?", "Pikachu", "The Prince", "Charles Francis Xavier", "Billy Bob Joe", 3);
        Quiz marvelSuperHeroesQ4 = new Quiz("What is Iceman's real name?", "Spongebob", "Patrick", "Robert Louis Drake", "Jim", 3);
        Quiz marvelSuperHeroesQ5 = new Quiz("What is the color of Beast's fur?", "rainbow", "blue", "orange", "pink", 2);
        List<Quiz> marvelSuperHeroesQuizzes = new ArrayList<Quiz>();
        marvelSuperHeroesQuizzes.add(marvelSuperHeroesQ1);
        marvelSuperHeroesQuizzes.add(marvelSuperHeroesQ2);
        marvelSuperHeroesQuizzes.add(marvelSuperHeroesQ3);
        marvelSuperHeroesQuizzes.add(marvelSuperHeroesQ4);
        marvelSuperHeroesQuizzes.add(marvelSuperHeroesQ5);
        Topic marvelSuperHeroes = new Topic("Marvel Super Heroes", "POW POOOOOWWWWWWW", "POW POOOOWWWW BAM SMACK KABOOM HUMNA HUMNA HERP DERP CRASH ACHOO BARK MEOW WHACK QUACK",
                marvelSuperHeroesQuizzes, 5);
        allTopics.add(marvelSuperHeroes);

        return allTopics;
    }

}
