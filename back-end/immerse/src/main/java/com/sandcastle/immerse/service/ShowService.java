package com.sandcastle.immerse.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sandcastle.immerse.model.dto.ShowListResponse;
import com.sandcastle.immerse.model.dto.ShowRequest;
import com.sandcastle.immerse.model.dto.ShowResponse;
import com.sandcastle.immerse.model.entity.ShowEntity;
import com.sandcastle.immerse.model.enums.ShowProgress;
import com.sandcastle.immerse.repository.ShowRepository;

import lombok.RequiredArgsConstructor;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ShowService {

	private final CategoryService categoryService;
	private final ShowRepository showRepository;

	public List<ShowListResponse> getShows() {
		List<ShowEntity> shows = showRepository.findAll();
		return shows.stream().map(ShowListResponse::new).collect(Collectors.toList());
	}

	public List<ShowListResponse> findShowsByCategory(Long categoryId) {
		List<ShowEntity> shows = showRepository.findByCategory(categoryId);
		return shows.stream().map(ShowListResponse::new).collect(Collectors.toList());
	}

	public Optional<ShowResponse> findShow(Long id) {
		ShowEntity show = showRepository.findById(id)
			.orElseThrow(() -> new IllegalArgumentException("does not exist!"));

		ShowResponse res = ShowResponse.builder()
			.title(show.getTitle())
			.showId(show.getShowId())
			.startTime(show.getStartTime())
			.endTime(show.getEndTime())
			.date(show.getDate())
			.description(show.getDescription())
			.thumbnail(show.getThumbnail())
			.attendanceLimit(show.getAttendanceLimit())
			.maxAttendance(show.getMaxAttendance())
			.showProgress(show.getShowProgress())
			.category_id(show.getCategory().getCategoryId())
			.build();
		return Optional.of(res);
	}

	@Transactional
	public Long postShow(ShowRequest req) {

		ShowEntity show = ShowEntity.builder()
			.title(req.getTitle())
			.startTime(req.getStartTime())
			.endTime(req.getEndTime())
			.date(req.getDate())
			.description(req.getDescription())
			.thumbnail(req.getThumbnail())
			.price(req.getPrice())
			.attendanceLimit(req.getAttendanceLimit())
			.showProgress(ShowProgress.SCHEDULED)
			.category(categoryService.findCategory(req.getCategoryId()).get())
			.build();
		return showRepository.save(show).getShowId();
	}

	@Transactional
	public Long putShow(Long showId, ShowRequest req) {
		ShowEntity current_show = showRepository.findById(showId)
			.orElseThrow(() -> new IllegalArgumentException("does not exist!"));

		ShowEntity show = ShowEntity.builder()
			.showId(showId)
			.title(req.getTitle())
			.startTime(req.getStartTime())
			.endTime(req.getEndTime())
			.date(req.getDate())
			.description(req.getDescription())
			.thumbnail(req.getThumbnail())
			.price(req.getPrice())
			.attendanceLimit(req.getAttendanceLimit())
			.maxAttendance(current_show.getMaxAttendance())
			.showProgress(current_show.getShowProgress())
			.category(categoryService.findCategory(req.getCategoryId()).get())
			.build();
		return showRepository.save(show).getShowId();
	}
}