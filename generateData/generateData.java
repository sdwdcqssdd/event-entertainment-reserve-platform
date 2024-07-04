import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Random;

public class generateData {
    private static final String[] TOPICS = {"Artificial Intelligence", "Blockchain Technology", "Climate Change", "Space Exploration", "Biotechnology", "Renewable Energy"};
    private static final String[] LOCATIONS = {"三教108", "三教107", "一科报告厅", "工学院南楼", "工学院北楼", "图书馆报告厅", "文科楼报告厅", "润杨体育馆", "理学院一楼化学系C1038"};
    private static final String[] ORGANIZERS = {"计算机科学与技术", "数学系", "Environmental Society", "Medical Association", "Astronomy Club"};
    private static final String[] NAMES = {
        "The Cation Shuffle",
        "Exploring Quantum Entanglement",
        "The Future of Artificial Intelligence",
        "Climate Change: Challenges and Solutions",
        "Understanding Dark Matter",
        "The Power of Renewable Energy",
        "Advancements in Genetic Engineering",
        "The Impact of Cybersecurity Threats",
        "The Beauty of Mathematics",
        "Space Colonization: Challenges and Opportunities",
        "Unraveling the Mysteries of Black Holes",
        "The Art of Public Speaking",
        "The Evolution of Human Consciousness",
        "Unlocking the Secrets of the Human Brain",
        "The Role of Big Data in Society",
        "Revolutionizing Healthcare with Technology",
        "The Ethics of AI and Robotics",
        "Exploring Ancient Civilizations",
        "The Future of Work in the Digital Age",
        "The Science of Happiness",
        // Add more lecture titles as needed
    };
    private static final String[] descriptions = {
        "This lecture will explore the latest advancements in the field of ",
        "Join us for an insightful discussion on the impact of ",
        "Discover the future of technology with this fascinating lecture on ",
        "Learn about the challenges and opportunities in ",
        "Explore the intersection of science and society in this engaging lecture on ",
        "Delve into the complexities of ",
        "Gain new insights into ",
        "Get inspired by ",
        "Dive deep into the world of ",
        "Explore cutting-edge research in ",
        "Uncover the mysteries of ",
        "Embark on a journey through ",
        "Join us for a deep dive into ",
        "Discover the untold stories of ",
        "Get a glimpse into the future with ",
        "Explore the impact of innovation on ",
        "Examine the historical context of ",
        "Get a behind-the-scenes look at ",
        "Learn about the evolution of ",
        "Witness the revolution in ",
        // Add even more descriptions as needed
    };
    private static final int MAX_CAPACITY = 200;
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm");

    public static void main(String[] args) {
        int numOfLectures = 200; // Number of lectures to generate

        try (FileWriter writer = new FileWriter("lectures.csv")) {
            writer.append("Name,Description,Date,Start Time,End Time,Location,Organizer,Capacity\n");

            Random random = new Random();

            for (int i = 0; i < numOfLectures; i++) {
                String name = NAMES[random.nextInt(NAMES.length)];
                String description = generateRandomDescription();
                String date = generateRandomDate();
                String startTime = generateRandomTime();
                String endTime = generateRandomTimeAfter(startTime);
                String location = LOCATIONS[random.nextInt(LOCATIONS.length)];
                String organizer = ORGANIZERS[random.nextInt(ORGANIZERS.length)];
                int capacity = random.nextInt(MAX_CAPACITY) + 1;

                writer.append(String.join(",", name, description, date, startTime, endTime, location, organizer, String.valueOf(capacity)));
                writer.append("\n");
            }

            System.out.println("Lectures generated successfully.");
        } catch (IOException e) {
            System.err.println("Error writing to file: " + e.getMessage());
        }
    }
    private static String generateRandomDate() {
        LocalDate randomDate = LocalDate.of(2024, 4, 5).plusDays(new Random().nextInt(10)); // Random date within 10 days from April 5, 2024
        return randomDate.format(DATE_FORMATTER);
    }

    private static String generateRandomDescription() {
        // Sample descriptions
        
        Random random = new Random();
        String topic = TOPICS[random.nextInt(TOPICS.length)];
        String descriptionPrefix = descriptions[random.nextInt(descriptions.length)];
        return descriptionPrefix + topic;
    }

    private static String generateRandomTime() {
        // Sample times (from 9am to 6pm)
        int hour = 9 + new Random().nextInt(10);
        int minute = new Random().nextInt(60);
        return String.format("%02d:%02d", hour, minute);
    }

    private static String generateRandomTimeAfter(String startTime) {
        String[] startParts = startTime.split(":");
        int startHour = Integer.parseInt(startParts[0]);
        int startMinute = Integer.parseInt(startParts[1]);

        Random random = new Random();
        int duration = 1 + random.nextInt(4); // Duration between 1 to 4 hours

        int endHour = startHour + duration;
        int endMinute = startMinute + random.nextInt(60);

        if (endMinute >= 60) {
            endHour++;
            endMinute -= 60;
        }

        return String.format("%02d:%02d", endHour, endMinute);
    }
    
}
