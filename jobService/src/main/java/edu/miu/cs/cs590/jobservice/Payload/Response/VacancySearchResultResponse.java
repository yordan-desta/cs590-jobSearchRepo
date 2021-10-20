package edu.miu.cs.cs590.jobservice.Payload.Response;

import edu.miu.cs.cs590.jobservice.Models.Vacancy;
import edu.miu.cs.cs590.jobservice.Payload.Requests.SearchResult;

import java.util.List;

public class VacancySearchResultResponse implements SearchResult<Vacancy> {

	private List<Vacancy> result;
	private int totalCount;

	@Override
	public List<Vacancy> getResult() {
		return result;
	}

	@Override
	public int getCount() {
		return totalCount;
	}

}
