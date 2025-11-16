-- Migration V2: Fix class_enrollments foreign key and normalize class data
-- Date: 2025-11-16
-- Description:
--   1. Fix foreign key to point to class_schedules instead of classes
--   2. Normalize day_of_week values to English
--   3. Set all classes to active by default

-- 1. Remove old foreign key constraint
ALTER TABLE class_enrollments
DROP CONSTRAINT IF EXISTS fk_class_enrollments_class_id__id;

-- 2. Add new foreign key pointing to class_schedules
ALTER TABLE class_enrollments
ADD CONSTRAINT fk_class_enrollments_class_id__id
FOREIGN KEY (class_id)
REFERENCES class_schedules(id)
ON DELETE CASCADE;

-- 3. Normalize day_of_week values from Spanish to English
UPDATE class_schedules
SET day_of_week = 'MONDAY'
WHERE day_of_week = 'LUNES';

UPDATE class_schedules
SET day_of_week = 'TUESDAY'
WHERE day_of_week = 'MARTES';

UPDATE class_schedules
SET day_of_week = 'WEDNESDAY'
WHERE day_of_week = 'MIERCOLES' OR day_of_week = 'MIÉRCOLES';

UPDATE class_schedules
SET day_of_week = 'THURSDAY'
WHERE day_of_week = 'JUEVES';

UPDATE class_schedules
SET day_of_week = 'FRIDAY'
WHERE day_of_week = 'VIERNES';

UPDATE class_schedules
SET day_of_week = 'SATURDAY'
WHERE day_of_week = 'SABADO' OR day_of_week = 'SÁBADO';

UPDATE class_schedules
SET day_of_week = 'SUNDAY'
WHERE day_of_week = 'DOMINGO';

-- 4. Set all existing classes to active
UPDATE class_schedules
SET is_active = true
WHERE is_active IS NULL OR is_active = false;
