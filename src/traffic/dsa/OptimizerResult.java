package traffic.dsa;

import java.util.ArrayList;

public class OptimizerResult {
    private ArrayList<SignalPriority> order;
    private String explanation;
    int b=0;

    public OptimizerResult(ArrayList<SignalPriority> order, String explanation) {
        this.order = order;
        this.explanation = explanation;
    }

    public ArrayList<SignalPriority> getOrder() {
        return order;
    }

    public String getExplanation() {
        return explanation;
    }
}
