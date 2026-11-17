package kr.co.felici.remembering.util;

import kr.co.felici.remembering.dto.AlbumResponseDto;
import kr.co.felici.remembering.dto.MemorialPostResponse;
import kr.co.felici.remembering.domain.AlbumVideo;
import kr.co.felici.remembering.domain.MemorialPost;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface AlbumVideoMapper {

    public AlbumResponseDto toAlbumResponseDto(AlbumVideo albumVideo);

    public AlbumVideo map(AlbumResponseDto albumResponseDto);

    public MemorialPostResponse toMemorialPostResponse(MemorialPost memorialPost);
    public MemorialPost map(MemorialPostResponse memorialPostResponse);

}
