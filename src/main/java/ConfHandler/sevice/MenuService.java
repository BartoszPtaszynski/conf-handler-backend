package ConfHandler.sevice;

import ConfHandler.model.dto.MenuDto;
import ConfHandler.model.entity.Menu;
import ConfHandler.repositories.MenuRepository;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Slf4j
public class MenuService {

    @Autowired
    private MenuRepository menuRepository;

    @Cacheable(value = "retrieveMenuMap", unless = "#result == null")
    public Map<UUID, MenuDto> retrieveMenuMap() {
        log.info("retrieveMenuMap");
        return menuRepository.findAll().stream()
                .collect(Collectors.toMap(Menu::getId,
                        menu -> MenuDto.builder().header(menu.getHeader()).menuItems(menu.getItems()).build()));
    }

}
