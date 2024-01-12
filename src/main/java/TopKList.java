import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.apache.hadoop.io.Writable;
import org.apache.hadoop.io.WritableComparable;

public class TopKList implements WritableComparable<TopKList> {
    private List<TopK> topKList;

    public TopKList(List<TopK> topKList) {
        this.topKList = topKList;
    }

    public TopKList() {
        this.topKList = new ArrayList<>();
    }

    public TopKList(TopKList other) {
        this.topKList = new ArrayList<>(other.topKList);
    }

    public void addTopK(TopK topK) {
        topKList.add(new TopK(topK));
    }

    public List<TopK> getTopKList() {
        return topKList;
    }

    @Override
    public void write(DataOutput dataOutput) throws IOException {
        dataOutput.writeInt(topKList.size());
        for (TopK topK : topKList) {
            topK.write(dataOutput);
        }
    }

    @Override
    public void readFields(DataInput dataInput) throws IOException {
        int size = dataInput.readInt();
        topKList.clear();
        for (int i = 0; i < size; i++) {
            TopK topK = new TopK();
            topK.readFields(dataInput);
            topKList.add(topK);
        }
    }

    @Override
    public int compareTo(TopKList o) {
        // You need to implement the comparison logic based on your requirements
        // For example, you can compare based on the content of the lists
        // or any other criteria that make sense for your use case.
        return 0;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("[");
        for (TopK topK : topKList) {
            builder.append(topK.toString()).append(", ");
        }
        if (!topKList.isEmpty()) {
            builder.delete(builder.length() - 2, builder.length());
        }
        builder.append("]");
        return builder.toString();
    }
}
