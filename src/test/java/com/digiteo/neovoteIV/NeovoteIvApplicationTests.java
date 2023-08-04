package com.digiteo.neovoteIV;

import com.digiteo.neovoteIV.web.data.model.ProposalResultData;
import org.junit.jupiter.api.Test;
import org.junit.platform.commons.util.CollectionUtils;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
class NeovoteIvApplicationTests {

	@Test
	void contextLoads() {
	}

	/*
	@Test
	public void whenComparing_sortByVotes() {
		List<ProposalResultData> prdl = new ArrayList<>();
		ProposalResultData p1 = new ProposalResultData(
				"p1",
				"asd@asd.cl",
				true,
				10,
				12,
				LocalDateTime.of(2023, 8, 03, 14, 00));

		ProposalResultData p2 = new ProposalResultData(
				"p2",
				"asd@asd.cl",
				true,
				12,
				12,
				LocalDateTime.of(2023, 8, 03, 14, 00));

		ProposalResultData p3 = new ProposalResultData(
				"p3",
				"asd@asd.cl",
				true,
				5,
				12,
				LocalDateTime.of(2023, 8, 03, 14, 00));

		ProposalResultData p4 = new ProposalResultData(
				"p4",
				"asd@asd.cl",
				true,
				30,
				12,
				LocalDateTime.of(2023, 8, 03, 14, 00));

		ProposalResultData p5 = new ProposalResultData(
				"p5",
				"asd@asd.cl",
				true,
				1,
				12,
				LocalDateTime.of(2023, 8, 03, 14, 00));

		prdl.add(p1);
		prdl.add(p2);
		prdl.add(p3);
		prdl.add(p4);
		prdl.add(p5);

		List<ProposalResultData> sortedPrdl = new ArrayList<>();

		sortedPrdl.add(p4); // 30v
		sortedPrdl.add(p2); // 12v
		sortedPrdl.add(p1); // 10v
		sortedPrdl.add(p3); // 5v
		sortedPrdl.add(p5); // 1v

		Comparator<ProposalResultData> comparator
				= Comparator.comparing(ProposalResultData::getVotes);

		Collections.sort(prdl, comparator);
		Collections.reverse(prdl);

		System.out.println(prdl);
		System.out.println("========================================================================================");
		System.out.println(sortedPrdl);
		}
	 */
	}
