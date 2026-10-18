package functions;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class Event {

    String eventId;
    String eventDate;
    String eventName;
    String eventVenue;
    String organiserId;
    String organiser;

    public Event(
        String eventId,
        String eventDate,
        String eventName,
        String eventVenue,
        String organiserId
    ) {
        this.eventId = eventId;
        this.eventDate = eventDate;
        this.eventName = eventName;
        this.eventVenue = eventVenue;
        this.organiserId = organiserId;
    }

    public String getEventname() {
        return eventName;
    }

    public String getEventDate() {
        return eventDate;
    }

    public String getVenue() {
        return eventVenue;
    }

    public String getEventId() {
        return eventId;
    }

    public String getOrganiser() {
        return organiser;
    }

    public String getOrganiserId() {
        return organiserId;
    }

    public void addEvent() {

        String sql =
            "INSERT INTO events " +
            "(event_id, event_name, event_date, venue, organiser_id) " +
            "VALUES (?, ?, ?, ?, ?)";

        try (
            Connection connection =
                DBConnection.getConnection();

            PreparedStatement statement =
                connection.prepareStatement(sql)
        ) {

            statement.setString(
                1,
                eventId
            );

            statement.setString(
                2,
                eventName
            );

            statement.setString(
                3,
                eventDate
            );

            statement.setString(
                4,
                eventVenue
            );

            statement.setString(
                5,
                organiserId
            );

            statement.executeUpdate();

            System.out.println(
                "Event added successfully!"
            );

        } catch (SQLException e) {

            e.printStackTrace();
        }
    }


public int deleteEvent(boolean isAdmin) {

    String sql;

    if (isAdmin) {
        sql = "DELETE FROM events WHERE event_id = ?";
    } else {
        sql = "DELETE FROM events WHERE event_id = ? AND organiser_id = ?";
    }

    try (
        Connection connection = DBConnection.getConnection();
        PreparedStatement statement = connection.prepareStatement(sql)
    ) {

        statement.setString(1, eventId);

        if (!isAdmin) {
            statement.setString(2, organiserId);
        }

        return statement.executeUpdate();

    } catch (SQLException e) {
        e.printStackTrace();
        return -1;
    }
}



    public ArrayList<Event> viewEvents() {

        ArrayList<Event> events =
            new ArrayList<>();

        String sql =
            "SELECT e.event_id, " +
            "e.event_name, " +
            "e.event_date, " +
            "e.venue, " +
            "e.organiser_id, " +
            "o.organiser_name " +
            "FROM events e " +
            "JOIN organisers o " +
            "ON e.organiser_id = o.organiser_id";

        try (
            Connection connection =
                DBConnection.getConnection();

            PreparedStatement statement =
                connection.prepareStatement(sql);

            ResultSet result =
                statement.executeQuery()
        ) {

            while (result.next()) {

                String id =
                    result.getString(
                        "event_id"
                    );

                String name =
                    result.getString(
                        "event_name"
                    );

                String date =
                    result.getString(
                        "event_date"
                    );

                String venue =
                    result.getString(
                        "venue"
                    );

                String organiserId =
                    result.getString(
                        "organiser_id"
                    );

                String organiserName =
                    result.getString(
                        "organiser_name"
                    );

                Event event =
                    new Event(
                        id,
                        date,
                        name,
                        venue,
                        organiserId
                    );

                event.organiser =
                    organiserName;

                events.add(event);
            }

        } catch (SQLException e) {

            e.printStackTrace();
        }

        return events;
    }

    public void registerEvent(
        String studentId
    ) {

        String sql =
            "INSERT INTO registrations " +
            "(event_id, student_id, registration_date) " +
            "VALUES (?, ?, CURDATE())";

        try (
            Connection connection =
                DBConnection.getConnection();

            PreparedStatement statement =
                connection.prepareStatement(sql)
        ) {

            statement.setString(
                1,
                eventId
            );

            statement.setString(
                2,
                studentId
            );

            statement.executeUpdate();

            System.out.println(
                "Student registered successfully!"
            );

        } catch (SQLException e) {

            e.printStackTrace();
        }
    }
    public ArrayList<String[]> getRegisteredStudents() {

    ArrayList<String[]> students =
        new ArrayList<>();

    String sql =
        "SELECT s.student_id, s.student_name, s.email " +
        "FROM registrations r " +
        "JOIN students s " +
        "ON r.student_id = s.student_id " +
        "WHERE r.event_id = ?";

    try (
        Connection connection =
            DBConnection.getConnection();

        PreparedStatement statement =
            connection.prepareStatement(sql)
    ) {

        statement.setString(
            1,
            eventId
        );

        ResultSet result =
            statement.executeQuery();

        while (result.next()) {

            students.add(
                new String[] {
                    result.getString("student_id"),
                    result.getString("student_name"),
                    result.getString("email")
                }
            );
        }

    } catch (SQLException e) {

        e.printStackTrace();
    }

    return students;
}
}
