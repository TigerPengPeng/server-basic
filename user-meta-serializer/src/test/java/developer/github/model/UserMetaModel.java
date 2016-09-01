package developer.github.model;

import developer.github.annotation.UserMeta;
import lombok.Data;

import java.util.Arrays;
import java.util.List;

/**
 * @package: developer.github.model
 * @class:
 * @author: 黄鹏
 * @date: 2016年08月17日 下午6:08
 * @description:
 */
@Data
public class UserMetaModel {

    @UserMeta(name = "idMeta")
    private Long id;

    @UserMeta(name = "stringIdMeta")
    private String stringId;

    @UserMeta(name = "listIdsMeta")
    private List<Long> listIds;

    @UserMeta(name = "arrayIdsMeta")
    private Long[] arrayIds;

    private Long noMetaId;

    public static UserMetaModel getTestMetaModel() {
        UserMetaModel userMetaModel = new UserMetaModel();
        userMetaModel.setId(1000L);
        userMetaModel.setStringId("2000");
        userMetaModel.setListIds(Arrays.asList(3000L, 4000L, 5000L));
        userMetaModel.setArrayIds(new Long[] {6000L, 7000L});
        userMetaModel.setNoMetaId(8000L);
        return userMetaModel;
    }
}
