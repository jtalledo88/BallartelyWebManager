package pe.com.foxsoft.ballartelyweb.batch.processor;

import org.springframework.batch.item.ItemProcessor;

public class ComprasItemProcessor implements ItemProcessor<String, Integer>{

	@Override
	public Integer process(String in) throws Exception {
		Integer out = in.length();
		return out;
	}

}
