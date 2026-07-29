package com.amarogamedev.plantai.dto;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;

public record UpdatedUserContext(LocalDate date, DayOfWeek dayOfWeek, List<String> savedPlantNames) {}