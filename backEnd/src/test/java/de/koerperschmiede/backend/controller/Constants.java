package de.koerperschmiede.backend.controller;

import de.koerperschmiede.backend.util.Category;
import de.koerperschmiede.backend.util.Equipment;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

/**
 * Constants for integration tests
 */

public abstract class Constants {

    // base urls
    public static final String USER_CONTROLLER_BASE_URL = "/api/v1/users";
    public static final String GENERAL_EXERCISE_CONTROLLER_BASE_URL = "/api/v1/general-exercises";
    public static final String CUSTOM_EXERCISE_CONTROLLER_BASE_URL = "/api/v1/custom-exercises";
    public static final String TRAINING_PLAN_CONTROLLER_BASE_URL = "/api/v1/training-plans";
    public static final String TRAINING_SESSION_CONTROLLER_BASE_URL = "/api/v1/training-sessions";


    /* squad general exercise  */
    public static final UUID SQUAT_ID_GENERAL = java.util.UUID.randomUUID();
    public static final String SQUAT_NAME = "Squats";
    public static final List<Category> SQUAT_CATEGORIES_ENUM = List.of(Category.LOWER_BODY);
    public static final List<String> SQUAT_CATEGORIES_STRINGS = List.of("LOWER_BODY");
    public static final List<Equipment> SQUAT_EQUIPMENT_ENUM = List.of(Equipment.CHAIR);
    public static final List<String> SQUAT_EQUIPMENT_STRINGS = List.of("CHAIR");
    public static final String SQUAT_DESCRIPTION_SHORT = "Squats are a great exercise for your legs. Short";
    public static final String SQUAT_DESCRIPTION_LONG = "Squats are a great exercise for your legs. Long";
    public static final String SQUAT_DIRECTIONS = "Do squats";
    public static final String SQUAT_VIDEO = "https://www.youtube.com/watch?v=U3Hj5fS7G5Q";
    public static final String SQUAT_THUMBNAIL_URL = "https://www.youtube.com/watch?v=U3Hj5fS7G5Q";

    /* squad custom exercise */
    public static final UUID SQUAT_ID_CUSTOM = java.util.UUID.randomUUID();
    public static final int SQUAT_REPS = 12;
    public static final int SQUAT_SETS = 3;
    public static final int SQUAT_DURATION_IN_MINUTES = 15;
    public static final String SQUAT_TIP = "Go deep";

    /* training plan */
    public static final UUID TRAINING_PLAN_ID = java.util.UUID.randomUUID();
    public static final String TRAINING_PLAN_NAME = "Beginner Plan";
    public static final String TRAINING_PLAN_TIP = "Tip";
    public static final String TRAINING_PLAN_SHORT_DESCRIPTION = "Short Description";
    public static final String TRAINING_PLAN_LONG_DESCRIPTION = "Long Description";

    /* training session */
    public static final UUID TRAINING_SESSION_ID = java.util.UUID.randomUUID();
    public static final Instant TRAINING_SESSION_DATE = Instant.ofEpochMilli(1735686000); // 2025-01-01 00:00:00
    public static final String TRAINING_SESSION_NOTES = "No Problems";

    // general constants
    public static final String AUTH_HEADER = "Authorization";
    public static final String JWT_TOKEN_PREFIX = "Bearer ";

    // user constants
    public static final String EMAIL_JOHN = "john.doe@example.com";
    public static final String FIRST_NAME_JOHN = "John";
    public static final UUID USER_JOHN_ID = UUID.randomUUID();

    public static final String EMAIL_JANE = "jane.doe@example.com";
    public static final String FIRST_NAME_JANE = "Jane";
    public static final UUID USER_JANE_ID = UUID.randomUUID();

    public static final String EMAIL_ADMIN = "admin@admin.com";
    public static final String FIRST_NAME_ADMIN = "Admin";
    public static final UUID USER_ADMIN_ID = UUID.randomUUID();

    public static final String LAST_NAME_DOE = "Doe";
    public static final String PASSWORD = "password";
}
