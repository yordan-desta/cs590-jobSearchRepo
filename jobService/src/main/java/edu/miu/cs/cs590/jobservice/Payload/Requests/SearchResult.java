package edu.miu.cs.cs590.jobservice.Payload.Requests;

import java.util.List;

public interface SearchResult<T> {
	List<T> getResult();

	int getCount();
}
