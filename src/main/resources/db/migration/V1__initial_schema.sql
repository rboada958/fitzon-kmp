--
-- PostgreSQL database dump
--

\restrict yUadc8waZXEKMULkwikMpDlAGuFQ2xZjcAFg12reHJf756c5sk1oxUFKNtdzUIj

-- Dumped from database version 15.14 (Debian 15.14-1.pgdg13+1)
-- Dumped by pg_dump version 18.0

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET transaction_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET xmloption = content;
SET client_min_messages = warning;
SET row_security = off;

SET default_tablespace = '';

SET default_table_access_method = heap;

--
-- Name: achievements; Type: TABLE; Schema: public; Owner: fitzon_user
--

CREATE TABLE public.achievements (
    id uuid NOT NULL,
    athlete_id uuid NOT NULL,
    icon character varying(10) NOT NULL,
    name character varying(255) NOT NULL,
    description character varying(500) NOT NULL,
    is_unlocked boolean DEFAULT false NOT NULL,
    unlocked_at timestamp without time zone
);


ALTER TABLE public.achievements OWNER TO fitzon_user;

--
-- Name: athletes; Type: TABLE; Schema: public; Owner: fitzon_user
--

CREATE TABLE public.athletes (
    id uuid NOT NULL,
    user_id uuid NOT NULL,
    box_id uuid,
    age integer,
    weight double precision,
    height double precision,
    bio character varying(500),
    joined_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP NOT NULL,
    membership_type character varying(50) DEFAULT 'Basic'::character varying,
    status character varying(50) DEFAULT 'PENDING'::character varying,
    payment_status character varying(50) DEFAULT 'PENDING'::character varying
);


ALTER TABLE public.athletes OWNER TO fitzon_user;

--
-- Name: boxes; Type: TABLE; Schema: public; Owner: fitzon_user
--

CREATE TABLE public.boxes (
    id uuid NOT NULL,
    name character varying(255) NOT NULL,
    description character varying(1000),
    location character varying(255) NOT NULL,
    phone character varying(20) NOT NULL,
    owner_id uuid NOT NULL,
    logo_url character varying(500),
    created_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP NOT NULL,
    email character varying(255),
    schedule character varying(500),
    rating real DEFAULT 0.0,
    total_reviews integer DEFAULT 0,
    amenities character varying(1000) DEFAULT '[]'::character varying,
    photos character varying(1000) DEFAULT '[]'::character varying
);


ALTER TABLE public.boxes OWNER TO fitzon_user;

--
-- Name: class_enrollments; Type: TABLE; Schema: public; Owner: fitzon_user
--

CREATE TABLE public.class_enrollments (
    id uuid NOT NULL,
    class_id uuid NOT NULL,
    athlete_id uuid NOT NULL,
    enrolled_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP NOT NULL
);


ALTER TABLE public.class_enrollments OWNER TO fitzon_user;

--
-- Name: class_schedules; Type: TABLE; Schema: public; Owner: fitzon_user
--

CREATE TABLE public.class_schedules (
    id uuid NOT NULL,
    box_id uuid NOT NULL,
    coach_id uuid NOT NULL,
    name character varying(255) NOT NULL,
    description character varying(500),
    start_time character varying(10) NOT NULL,
    end_time character varying(10) NOT NULL,
    day_of_week character varying(10) NOT NULL,
    max_capacity integer NOT NULL,
    current_enrollment integer DEFAULT 0 NOT NULL,
    level character varying(50) DEFAULT 'BEGINNER'::character varying NOT NULL,
    is_active boolean DEFAULT true NOT NULL,
    created_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP NOT NULL,
    "time" character varying(10)
);


ALTER TABLE public.class_schedules OWNER TO fitzon_user;

--
-- Name: classes; Type: TABLE; Schema: public; Owner: fitzon_user
--

CREATE TABLE public.classes (
    id uuid NOT NULL,
    box_id uuid NOT NULL,
    coach_id uuid NOT NULL,
    name character varying(255) NOT NULL,
    description character varying(500),
    start_time character varying(10) NOT NULL,
    end_time character varying(10) NOT NULL,
    day_of_week character varying(10) NOT NULL,
    max_capacity integer NOT NULL,
    level character varying(50) DEFAULT 'BEGINNER'::character varying NOT NULL,
    created_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP NOT NULL
);


ALTER TABLE public.classes OWNER TO fitzon_user;

--
-- Name: coaches; Type: TABLE; Schema: public; Owner: fitzon_user
--

CREATE TABLE public.coaches (
    id uuid NOT NULL,
    user_id uuid NOT NULL,
    box_id uuid NOT NULL,
    specialties character varying(1000) DEFAULT '[]'::character varying NOT NULL,
    bio character varying(500),
    joined_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP NOT NULL,
    certifications character varying(1000) DEFAULT '[]'::character varying,
    status character varying(50) DEFAULT '[]'::character varying,
    rating real DEFAULT 0.0,
    total_reviews integer DEFAULT 0,
    years_experience integer DEFAULT 0,
    total_classes integer DEFAULT 0
);


ALTER TABLE public.coaches OWNER TO fitzon_user;

--
-- Name: exercises; Type: TABLE; Schema: public; Owner: fitzon_user
--

CREATE TABLE public.exercises (
    id uuid NOT NULL,
    workout_id uuid NOT NULL,
    name character varying(255) NOT NULL,
    sets integer NOT NULL,
    reps integer NOT NULL,
    weight character varying(50),
    notes character varying(500),
    video_url character varying(500)
);


ALTER TABLE public.exercises OWNER TO fitzon_user;

--
-- Name: flyway_schema_history; Type: TABLE; Schema: public; Owner: fitzon_user
--

CREATE TABLE public.flyway_schema_history (
    installed_rank integer NOT NULL,
    version character varying(50),
    description character varying(200) NOT NULL,
    type character varying(20) NOT NULL,
    script character varying(1000) NOT NULL,
    checksum integer,
    installed_by character varying(100) NOT NULL,
    installed_on timestamp without time zone DEFAULT now() NOT NULL,
    execution_time integer NOT NULL,
    success boolean NOT NULL
);


ALTER TABLE public.flyway_schema_history OWNER TO fitzon_user;

--
-- Name: membership_renewals; Type: TABLE; Schema: public; Owner: fitzon_user
--

CREATE TABLE public.membership_renewals (
    id uuid NOT NULL,
    athlete_id uuid NOT NULL,
    expires_at timestamp without time zone NOT NULL,
    status character varying(50) DEFAULT 'PENDING'::character varying NOT NULL,
    notified_at timestamp without time zone
);


ALTER TABLE public.membership_renewals OWNER TO fitzon_user;

--
-- Name: notifications; Type: TABLE; Schema: public; Owner: fitzon_user
--

CREATE TABLE public.notifications (
    id uuid NOT NULL,
    user_id uuid NOT NULL,
    title character varying(255) NOT NULL,
    message character varying(1000) NOT NULL,
    type character varying(50) NOT NULL,
    is_read boolean DEFAULT false NOT NULL,
    created_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP NOT NULL
);


ALTER TABLE public.notifications OWNER TO fitzon_user;

--
-- Name: personal_records; Type: TABLE; Schema: public; Owner: fitzon_user
--

CREATE TABLE public.personal_records (
    id uuid NOT NULL,
    athlete_id uuid NOT NULL,
    exercise_name character varying(255) NOT NULL,
    value character varying(100) NOT NULL,
    unit character varying(50) NOT NULL,
    achieved_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP NOT NULL
);


ALTER TABLE public.personal_records OWNER TO fitzon_user;

--
-- Name: users; Type: TABLE; Schema: public; Owner: fitzon_user
--

CREATE TABLE public.users (
    id uuid NOT NULL,
    email character varying(255) NOT NULL,
    password_hash character varying(255) NOT NULL,
    name character varying(255),
    role character varying(50) DEFAULT 'ATHLETE'::character varying NOT NULL,
    profile_image_url character varying(500),
    created_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP NOT NULL
);


ALTER TABLE public.users OWNER TO fitzon_user;

--
-- Name: workout_logs; Type: TABLE; Schema: public; Owner: fitzon_user
--

CREATE TABLE public.workout_logs (
    id uuid NOT NULL,
    athlete_id uuid NOT NULL,
    workout_id uuid,
    completed_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP NOT NULL,
    calories_burned integer,
    duration_minutes integer,
    notes character varying(500)
);


ALTER TABLE public.workout_logs OWNER TO fitzon_user;

--
-- Name: workouts; Type: TABLE; Schema: public; Owner: fitzon_user
--

CREATE TABLE public.workouts (
    id uuid NOT NULL,
    class_id uuid,
    box_id uuid NOT NULL,
    title character varying(255) NOT NULL,
    description character varying(1000),
    date date NOT NULL,
    duration integer NOT NULL,
    difficulty character varying(50) NOT NULL,
    created_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP NOT NULL
);


ALTER TABLE public.workouts OWNER TO fitzon_user;

--
-- Name: achievements achievements_pkey; Type: CONSTRAINT; Schema: public; Owner: fitzon_user
--

ALTER TABLE ONLY public.achievements
    ADD CONSTRAINT achievements_pkey PRIMARY KEY (id);


--
-- Name: athletes athletes_pkey; Type: CONSTRAINT; Schema: public; Owner: fitzon_user
--

ALTER TABLE ONLY public.athletes
    ADD CONSTRAINT athletes_pkey PRIMARY KEY (id);


--
-- Name: boxes boxes_pkey; Type: CONSTRAINT; Schema: public; Owner: fitzon_user
--

ALTER TABLE ONLY public.boxes
    ADD CONSTRAINT boxes_pkey PRIMARY KEY (id);


--
-- Name: class_enrollments class_enrollments_class_id_athlete_id_unique; Type: CONSTRAINT; Schema: public; Owner: fitzon_user
--

ALTER TABLE ONLY public.class_enrollments
    ADD CONSTRAINT class_enrollments_class_id_athlete_id_unique UNIQUE (class_id, athlete_id);


--
-- Name: class_enrollments class_enrollments_pkey; Type: CONSTRAINT; Schema: public; Owner: fitzon_user
--

ALTER TABLE ONLY public.class_enrollments
    ADD CONSTRAINT class_enrollments_pkey PRIMARY KEY (id);


--
-- Name: class_schedules class_schedules_pkey; Type: CONSTRAINT; Schema: public; Owner: fitzon_user
--

ALTER TABLE ONLY public.class_schedules
    ADD CONSTRAINT class_schedules_pkey PRIMARY KEY (id);


--
-- Name: classes classes_pkey; Type: CONSTRAINT; Schema: public; Owner: fitzon_user
--

ALTER TABLE ONLY public.classes
    ADD CONSTRAINT classes_pkey PRIMARY KEY (id);


--
-- Name: coaches coaches_pkey; Type: CONSTRAINT; Schema: public; Owner: fitzon_user
--

ALTER TABLE ONLY public.coaches
    ADD CONSTRAINT coaches_pkey PRIMARY KEY (id);


--
-- Name: exercises exercises_pkey; Type: CONSTRAINT; Schema: public; Owner: fitzon_user
--

ALTER TABLE ONLY public.exercises
    ADD CONSTRAINT exercises_pkey PRIMARY KEY (id);


--
-- Name: flyway_schema_history flyway_schema_history_pk; Type: CONSTRAINT; Schema: public; Owner: fitzon_user
--

ALTER TABLE ONLY public.flyway_schema_history
    ADD CONSTRAINT flyway_schema_history_pk PRIMARY KEY (installed_rank);


--
-- Name: membership_renewals membership_renewals_pkey; Type: CONSTRAINT; Schema: public; Owner: fitzon_user
--

ALTER TABLE ONLY public.membership_renewals
    ADD CONSTRAINT membership_renewals_pkey PRIMARY KEY (id);


--
-- Name: notifications notifications_pkey; Type: CONSTRAINT; Schema: public; Owner: fitzon_user
--

ALTER TABLE ONLY public.notifications
    ADD CONSTRAINT notifications_pkey PRIMARY KEY (id);


--
-- Name: personal_records personal_records_pkey; Type: CONSTRAINT; Schema: public; Owner: fitzon_user
--

ALTER TABLE ONLY public.personal_records
    ADD CONSTRAINT personal_records_pkey PRIMARY KEY (id);


--
-- Name: users users_email_unique; Type: CONSTRAINT; Schema: public; Owner: fitzon_user
--

ALTER TABLE ONLY public.users
    ADD CONSTRAINT users_email_unique UNIQUE (email);


--
-- Name: users users_pkey; Type: CONSTRAINT; Schema: public; Owner: fitzon_user
--

ALTER TABLE ONLY public.users
    ADD CONSTRAINT users_pkey PRIMARY KEY (id);


--
-- Name: workout_logs workout_logs_pkey; Type: CONSTRAINT; Schema: public; Owner: fitzon_user
--

ALTER TABLE ONLY public.workout_logs
    ADD CONSTRAINT workout_logs_pkey PRIMARY KEY (id);


--
-- Name: workouts workouts_pkey; Type: CONSTRAINT; Schema: public; Owner: fitzon_user
--

ALTER TABLE ONLY public.workouts
    ADD CONSTRAINT workouts_pkey PRIMARY KEY (id);


--
-- Name: flyway_schema_history_s_idx; Type: INDEX; Schema: public; Owner: fitzon_user
--

CREATE INDEX flyway_schema_history_s_idx ON public.flyway_schema_history USING btree (success);


--
-- Name: achievements fk_achievements_athlete_id__id; Type: FK CONSTRAINT; Schema: public; Owner: fitzon_user
--

ALTER TABLE ONLY public.achievements
    ADD CONSTRAINT fk_achievements_athlete_id__id FOREIGN KEY (athlete_id) REFERENCES public.athletes(id) ON UPDATE RESTRICT ON DELETE RESTRICT;


--
-- Name: athletes fk_athletes_box_id__id; Type: FK CONSTRAINT; Schema: public; Owner: fitzon_user
--

ALTER TABLE ONLY public.athletes
    ADD CONSTRAINT fk_athletes_box_id__id FOREIGN KEY (box_id) REFERENCES public.boxes(id) ON UPDATE RESTRICT ON DELETE RESTRICT;


--
-- Name: athletes fk_athletes_user_id__id; Type: FK CONSTRAINT; Schema: public; Owner: fitzon_user
--

ALTER TABLE ONLY public.athletes
    ADD CONSTRAINT fk_athletes_user_id__id FOREIGN KEY (user_id) REFERENCES public.users(id) ON UPDATE RESTRICT ON DELETE RESTRICT;


--
-- Name: boxes fk_boxes_owner_id__id; Type: FK CONSTRAINT; Schema: public; Owner: fitzon_user
--

ALTER TABLE ONLY public.boxes
    ADD CONSTRAINT fk_boxes_owner_id__id FOREIGN KEY (owner_id) REFERENCES public.users(id) ON UPDATE RESTRICT ON DELETE RESTRICT;


--
-- Name: class_enrollments fk_class_enrollments_athlete_id__id; Type: FK CONSTRAINT; Schema: public; Owner: fitzon_user
--

ALTER TABLE ONLY public.class_enrollments
    ADD CONSTRAINT fk_class_enrollments_athlete_id__id FOREIGN KEY (athlete_id) REFERENCES public.athletes(id) ON UPDATE RESTRICT ON DELETE RESTRICT;


--
-- Name: class_enrollments fk_class_enrollments_class_id__id; Type: FK CONSTRAINT; Schema: public; Owner: fitzon_user
--

ALTER TABLE ONLY public.class_enrollments
    ADD CONSTRAINT fk_class_enrollments_class_id__id FOREIGN KEY (class_id) REFERENCES public.classes(id) ON UPDATE RESTRICT ON DELETE RESTRICT;


--
-- Name: class_schedules fk_class_schedules_box_id__id; Type: FK CONSTRAINT; Schema: public; Owner: fitzon_user
--

ALTER TABLE ONLY public.class_schedules
    ADD CONSTRAINT fk_class_schedules_box_id__id FOREIGN KEY (box_id) REFERENCES public.boxes(id) ON UPDATE RESTRICT ON DELETE RESTRICT;


--
-- Name: class_schedules fk_class_schedules_coach_id__id; Type: FK CONSTRAINT; Schema: public; Owner: fitzon_user
--

ALTER TABLE ONLY public.class_schedules
    ADD CONSTRAINT fk_class_schedules_coach_id__id FOREIGN KEY (coach_id) REFERENCES public.coaches(id) ON UPDATE RESTRICT ON DELETE RESTRICT;


--
-- Name: classes fk_classes_box_id__id; Type: FK CONSTRAINT; Schema: public; Owner: fitzon_user
--

ALTER TABLE ONLY public.classes
    ADD CONSTRAINT fk_classes_box_id__id FOREIGN KEY (box_id) REFERENCES public.boxes(id) ON UPDATE RESTRICT ON DELETE RESTRICT;


--
-- Name: classes fk_classes_coach_id__id; Type: FK CONSTRAINT; Schema: public; Owner: fitzon_user
--

ALTER TABLE ONLY public.classes
    ADD CONSTRAINT fk_classes_coach_id__id FOREIGN KEY (coach_id) REFERENCES public.coaches(id) ON UPDATE RESTRICT ON DELETE RESTRICT;


--
-- Name: coaches fk_coaches_box_id__id; Type: FK CONSTRAINT; Schema: public; Owner: fitzon_user
--

ALTER TABLE ONLY public.coaches
    ADD CONSTRAINT fk_coaches_box_id__id FOREIGN KEY (box_id) REFERENCES public.boxes(id) ON UPDATE RESTRICT ON DELETE RESTRICT;


--
-- Name: coaches fk_coaches_user_id__id; Type: FK CONSTRAINT; Schema: public; Owner: fitzon_user
--

ALTER TABLE ONLY public.coaches
    ADD CONSTRAINT fk_coaches_user_id__id FOREIGN KEY (user_id) REFERENCES public.users(id) ON UPDATE RESTRICT ON DELETE RESTRICT;


--
-- Name: exercises fk_exercises_workout_id__id; Type: FK CONSTRAINT; Schema: public; Owner: fitzon_user
--

ALTER TABLE ONLY public.exercises
    ADD CONSTRAINT fk_exercises_workout_id__id FOREIGN KEY (workout_id) REFERENCES public.workouts(id) ON UPDATE RESTRICT ON DELETE RESTRICT;


--
-- Name: membership_renewals fk_membership_renewals_athlete_id__id; Type: FK CONSTRAINT; Schema: public; Owner: fitzon_user
--

ALTER TABLE ONLY public.membership_renewals
    ADD CONSTRAINT fk_membership_renewals_athlete_id__id FOREIGN KEY (athlete_id) REFERENCES public.athletes(id) ON UPDATE RESTRICT ON DELETE RESTRICT;


--
-- Name: notifications fk_notifications_user_id__id; Type: FK CONSTRAINT; Schema: public; Owner: fitzon_user
--

ALTER TABLE ONLY public.notifications
    ADD CONSTRAINT fk_notifications_user_id__id FOREIGN KEY (user_id) REFERENCES public.users(id) ON UPDATE RESTRICT ON DELETE RESTRICT;


--
-- Name: personal_records fk_personal_records_athlete_id__id; Type: FK CONSTRAINT; Schema: public; Owner: fitzon_user
--

ALTER TABLE ONLY public.personal_records
    ADD CONSTRAINT fk_personal_records_athlete_id__id FOREIGN KEY (athlete_id) REFERENCES public.athletes(id) ON UPDATE RESTRICT ON DELETE RESTRICT;


--
-- Name: workout_logs fk_workout_logs_athlete_id__id; Type: FK CONSTRAINT; Schema: public; Owner: fitzon_user
--

ALTER TABLE ONLY public.workout_logs
    ADD CONSTRAINT fk_workout_logs_athlete_id__id FOREIGN KEY (athlete_id) REFERENCES public.athletes(id) ON UPDATE RESTRICT ON DELETE RESTRICT;


--
-- Name: workout_logs fk_workout_logs_workout_id__id; Type: FK CONSTRAINT; Schema: public; Owner: fitzon_user
--

ALTER TABLE ONLY public.workout_logs
    ADD CONSTRAINT fk_workout_logs_workout_id__id FOREIGN KEY (workout_id) REFERENCES public.workouts(id) ON UPDATE RESTRICT ON DELETE RESTRICT;


--
-- Name: workouts fk_workouts_box_id__id; Type: FK CONSTRAINT; Schema: public; Owner: fitzon_user
--

ALTER TABLE ONLY public.workouts
    ADD CONSTRAINT fk_workouts_box_id__id FOREIGN KEY (box_id) REFERENCES public.boxes(id) ON UPDATE RESTRICT ON DELETE RESTRICT;


--
-- Name: workouts fk_workouts_class_id__id; Type: FK CONSTRAINT; Schema: public; Owner: fitzon_user
--

ALTER TABLE ONLY public.workouts
    ADD CONSTRAINT fk_workouts_class_id__id FOREIGN KEY (class_id) REFERENCES public.classes(id) ON UPDATE RESTRICT ON DELETE RESTRICT;


--
-- PostgreSQL database dump complete
--

\unrestrict yUadc8waZXEKMULkwikMpDlAGuFQ2xZjcAFg12reHJf756c5sk1oxUFKNtdzUIj

