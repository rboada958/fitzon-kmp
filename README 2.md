# fitzon-kmp

# clean gradle to create commits with Gemini

- git restore .gradle build
- git restore --staged .gradle build

# Alter table Render

- psql "postgresql://fitzon_db_user:2FNPjUaGPNXxsxU88A4LDqpS9X81YjEF@dpg-d3rrjfjuibrs73b84od0-a.oregon-postgres.render.com/fitzon_db" -c "ALTER TABLE class_schedules ADD COLUMN workout_id UUID NULL; ALTER TABLE class_schedules ADD CONSTRAINT fk_class_schedules_workout_id FOREIGN KEY (workout_id) REFERENCES workouts(id) ON DELETE SET NULL;"

# Alter table localhost

- docker exec -it fitzon_db psql -U fitzon_user -d fitzon_db -c "ALTER TABLE class_schedules ADD COLUMN workout_id UUID NULL; ALTER TABLE class_schedules ADD CONSTRAINT fk_class_schedules_workout_id FOREIGN KEY (workout_id) REFERENCES workouts(id) ON DELETE SET NULL;"