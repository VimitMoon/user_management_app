package in.vi.user_management_app.dto;

import java.util.List;

public class QuoteApiResponse {

    private QuoteDto[] quotes;

    public QuoteDto[] getQuotes() {
        return quotes;
    }

    public void setQuotes(QuoteDto[] quotes) {
        this.quotes = quotes;
    }
}
