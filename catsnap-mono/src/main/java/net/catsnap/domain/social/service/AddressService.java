package net.catsnap.domain.social.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import net.catsnap.domain.reservation.entity.DistrictLevel;
import net.catsnap.domain.reservation.entity.TownLevel;
import net.catsnap.domain.reservation.repository.CityLevelRepository;
import net.catsnap.domain.reservation.repository.DistrictLevelRepository;
import net.catsnap.domain.reservation.repository.TownLevelRepository;
import net.catsnap.domain.social.dto.response.AddressListResponse;
import net.catsnap.domain.social.dto.response.AddressResponse;
import net.catsnap.global.result.SlicedData;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AddressService {

    private final CityLevelRepository cityLevelRepository;
    private final DistrictLevelRepository districtLevelRepository;
    private final TownLevelRepository townLevelRepository;

    @Transactional(readOnly = true)
    public AddressListResponse getCityLevel() {
        List<AddressResponse> addressResponseList = cityLevelRepository.findAll().stream()
            .map(AddressResponse::from)
            .toList();
        return AddressListResponse.of(addressResponseList);
    }

    @Transactional(readOnly = true)
    public SlicedData<AddressListResponse> getDistrictLevel(Long cityLevelId, Pageable pageable) {
        Slice<DistrictLevel> districtLevelSlice = districtLevelRepository.findAllByCityLevelId(
            cityLevelId, pageable);
        List<AddressResponse> addressResponseList = districtLevelSlice.getContent()
            .stream()
            .map(AddressResponse::from)
            .toList();
        return SlicedData.of(AddressListResponse.of(addressResponseList),
            districtLevelSlice.isFirst(), districtLevelSlice.isLast());
    }

    @Transactional(readOnly = true)
    public SlicedData<AddressListResponse> getTownLevel(Long districtLevelId, Pageable pageable) {
        Slice<TownLevel> townLevelSlice = townLevelRepository
            .findAllByDistrictLevelId(districtLevelId, pageable);
        List<AddressResponse> addressResponseList = townLevelSlice.getContent()
            .stream()
            .map(AddressResponse::from)
            .toList();
        return SlicedData.of(AddressListResponse.of(addressResponseList),
            townLevelSlice.isFirst(), townLevelSlice.isLast());
    }
}
