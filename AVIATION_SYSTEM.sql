--
-- Base de datos: `AVIATION_SYSTEM`
--

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `AIRCRAFT`
--

CREATE TABLE `AIRCRAFT` (
                            `AIRCRAFT_ID` int NOT NULL,
                            `AIRLINE_ID` int NOT NULL,
                            `MODEL` varchar(100) NOT NULL,
                            `MANUFACTURER` varchar(100) NOT NULL,
                            `REGISTRATION_NUMBER` varchar(20) NOT NULL,
                            `PRODUCTION_YEAR` year NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `AIRCRAFT_DETAILS`
--

CREATE TABLE `AIRCRAFT_DETAILS` (
                                    `AIRCRAFT_ID` int NOT NULL,
                                    `PASSENGER_CAPACITY` int NOT NULL,
                                    `MAX_RANGE_KM` int NOT NULL,
                                    `MAX_SPEED_KMH` int NOT NULL,
                                    `FUEL_CAPACITY_LITERS` int NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `AIRLINES`
--

CREATE TABLE `AIRLINES` (
                            `AIRLINE_ID` int NOT NULL,
                            `NAME` varchar(100) NOT NULL,
                            `IATA_CODE` char(2) NOT NULL,
                            `ICAO_CODE` char(3) NOT NULL,
                            `COUNTRY` varchar(50) NOT NULL,
                            `FOUNDATION_YEAR` year DEFAULT NULL,
                            `WEBSITE` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `FLIGHTS`
--

CREATE TABLE `FLIGHTS` (
                           `FLIGHT_ID` int NOT NULL,
                           `AIRLINE_ID` int NOT NULL,
                           `AIRCRAFT_ID` int NOT NULL,
                           `FLIGHT_NUMBER` varchar(10) NOT NULL,
                           `DEPARTURE_AIRPORT` varchar(4) NOT NULL,
                           `ARRIVAL_AIRPORT` varchar(4) NOT NULL,
                           `SCHEDULED_DEPARTURE` datetime NOT NULL,
                           `SCHEDULED_ARRIVAL` datetime NOT NULL,
                           `STATUS` enum('SCHEDULED','DEPARTED','IN_AIR','LANDED','CANCELLED') NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `PILOTS`
--

CREATE TABLE `PILOTS` (
                          `PILOT_ID` int NOT NULL,
                          `AIRLINE_ID` int NOT NULL,
                          `FIRST_NAME` varchar(50) NOT NULL,
                          `LAST_NAME` varchar(100) NOT NULL,
                          `DATE_OF_BIRTH` date NOT NULL,
                          `NATIONALITY` varchar(50) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `PILOT_ASSIGNMENTS`
--

CREATE TABLE `PILOT_ASSIGNMENTS` (
                                     `ASSIGNMENT_ID` int NOT NULL,
                                     `FLIGHT_ID` int NOT NULL,
                                     `PILOT_ID` int NOT NULL,
                                     `ROLE` enum('CAPTAIN','FIRST_OFFICER','TRAINEE') NOT NULL,
                                     `IS_LEAD_PILOT` tinyint(1) DEFAULT '0',
                                     `ASSIGNED_HOURS` decimal(5,2) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `PILOT_LICENSES`
--

CREATE TABLE `PILOT_LICENSES` (
                                  `PILOT_ID` int NOT NULL,
                                  `LICENSE_NUMBER` varchar(20) NOT NULL,
                                  `LICENSE_TYPE` enum('ATPL','CPL','MPL') NOT NULL,
                                  `ISSUE_DATE` date NOT NULL,
                                  `EXPIRATION_DATE` date NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Índices para tablas volcadas
--

--
-- Indices de la tabla `AIRCRAFT`
--
ALTER TABLE `AIRCRAFT`
    ADD PRIMARY KEY (`AIRCRAFT_ID`),
  ADD UNIQUE KEY `REGISTRATION_NUMBER` (`REGISTRATION_NUMBER`),
  ADD KEY `AIRLINE_ID` (`AIRLINE_ID`);

--
-- Indices de la tabla `AIRCRAFT_DETAILS`
--
ALTER TABLE `AIRCRAFT_DETAILS`
    ADD PRIMARY KEY (`AIRCRAFT_ID`);

--
-- Indices de la tabla `AIRLINES`
--
ALTER TABLE `AIRLINES`
    ADD PRIMARY KEY (`AIRLINE_ID`),
  ADD UNIQUE KEY `IATA_CODE` (`IATA_CODE`),
  ADD UNIQUE KEY `ICAO_CODE` (`ICAO_CODE`);

--
-- Indices de la tabla `FLIGHTS`
--
ALTER TABLE `FLIGHTS`
    ADD PRIMARY KEY (`FLIGHT_ID`),
  ADD KEY `AIRLINE_ID` (`AIRLINE_ID`),
  ADD KEY `AIRCRAFT_ID` (`AIRCRAFT_ID`);

--
-- Indices de la tabla `PILOTS`
--
ALTER TABLE `PILOTS`
    ADD PRIMARY KEY (`PILOT_ID`),
  ADD KEY `AIRLINE_ID` (`AIRLINE_ID`);

--
-- Indices de la tabla `PILOT_ASSIGNMENTS`
--
ALTER TABLE `PILOT_ASSIGNMENTS`
    ADD PRIMARY KEY (`ASSIGNMENT_ID`),
  ADD UNIQUE KEY `FLIGHT_ID` (`FLIGHT_ID`,`PILOT_ID`,`ROLE`),
  ADD KEY `PILOT_ID` (`PILOT_ID`);

--
-- Indices de la tabla `PILOT_LICENSES`
--
ALTER TABLE `PILOT_LICENSES`
    ADD PRIMARY KEY (`PILOT_ID`),
  ADD UNIQUE KEY `LICENSE_NUMBER` (`LICENSE_NUMBER`);

--
-- AUTO_INCREMENT de las tablas volcadas
--

--
-- AUTO_INCREMENT de la tabla `AIRCRAFT`
--
ALTER TABLE `AIRCRAFT`
    MODIFY `AIRCRAFT_ID` int NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT de la tabla `AIRLINES`
--
ALTER TABLE `AIRLINES`
    MODIFY `AIRLINE_ID` int NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT de la tabla `FLIGHTS`
--
ALTER TABLE `FLIGHTS`
    MODIFY `FLIGHT_ID` int NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT de la tabla `PILOTS`
--
ALTER TABLE `PILOTS`
    MODIFY `PILOT_ID` int NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT de la tabla `PILOT_ASSIGNMENTS`
--
ALTER TABLE `PILOT_ASSIGNMENTS`
    MODIFY `ASSIGNMENT_ID` int NOT NULL AUTO_INCREMENT;

--
-- Restricciones para tablas volcadas
--

--
-- Filtros para la tabla `AIRCRAFT`
--
ALTER TABLE `AIRCRAFT`
    ADD CONSTRAINT `AIRCRAFT_ibfk_1` FOREIGN KEY (`AIRLINE_ID`) REFERENCES `AIRLINES` (`AIRLINE_ID`);

--
-- Filtros para la tabla `AIRCRAFT_DETAILS`
--
ALTER TABLE `AIRCRAFT_DETAILS`
    ADD CONSTRAINT `AIRCRAFT_DETAILS_ibfk_1` FOREIGN KEY (`AIRCRAFT_ID`) REFERENCES `AIRCRAFT` (`AIRCRAFT_ID`) ON DELETE CASCADE;

--
-- Filtros para la tabla `FLIGHTS`
--
ALTER TABLE `FLIGHTS`
    ADD CONSTRAINT `FLIGHTS_ibfk_1` FOREIGN KEY (`AIRLINE_ID`) REFERENCES `AIRLINES` (`AIRLINE_ID`),
  ADD CONSTRAINT `FLIGHTS_ibfk_2` FOREIGN KEY (`AIRCRAFT_ID`) REFERENCES `AIRCRAFT` (`AIRCRAFT_ID`);

--
-- Filtros para la tabla `PILOTS`
--
ALTER TABLE `PILOTS`
    ADD CONSTRAINT `PILOTS_ibfk_1` FOREIGN KEY (`AIRLINE_ID`) REFERENCES `AIRLINES` (`AIRLINE_ID`);

--
-- Filtros para la tabla `PILOT_ASSIGNMENTS`
--
ALTER TABLE `PILOT_ASSIGNMENTS`
    ADD CONSTRAINT `PILOT_ASSIGNMENTS_ibfk_1` FOREIGN KEY (`FLIGHT_ID`) REFERENCES `FLIGHTS` (`FLIGHT_ID`),
  ADD CONSTRAINT `PILOT_ASSIGNMENTS_ibfk_2` FOREIGN KEY (`PILOT_ID`) REFERENCES `PILOTS` (`PILOT_ID`);

--
-- Filtros para la tabla `PILOT_LICENSES`
--
ALTER TABLE `PILOT_LICENSES`
    ADD CONSTRAINT `PILOT_LICENSES_ibfk_1` FOREIGN KEY (`PILOT_ID`) REFERENCES `PILOTS` (`PILOT_ID`) ON DELETE CASCADE;
COMMIT;
