package pro.bigbro.services.counting;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import pro.bigbro.models.reportUnits.total.DataTotal;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Component
@Lazy
public class CountingService {

    public double countAvgRevenueByAge(List<DataTotal> dataTotalList, List<DataTotal> age, int ageFrom, int ageTo) {
        List<Integer> cityList = age.stream()
                .filter(dataTotal -> dataTotal.getValue() >= ageFrom)
                .filter(dataTotal -> dataTotal.getValue() < ageTo)
                .map(dataTotal -> dataTotal.getCityId())
                .collect(Collectors.toList());

        return dataTotalList.stream()
                .filter(dataTotal -> cityList.contains(dataTotal.getCityId()))
                .mapToDouble(dataTotal -> dataTotal.getValue())
                .average()
                .orElse(0.0d);
    }

    public double countCitiesByRevenue(List<DataTotal> dataTotalList, int revenue) {
        return dataTotalList.stream()
                .filter(dataTotal -> dataTotal.getValue() >= revenue)
                .count();
    }

    public int yearForConversion(int year, int month, int i) {
        return LocalDate.of(year, month, 1)
                .minusMonths(i).getYear();
    }

    public int monthForConversion(int year, int month, int i) {
        return LocalDate.of(year, month, 1)
                .minusMonths(i).getMonthValue();
    }
}
