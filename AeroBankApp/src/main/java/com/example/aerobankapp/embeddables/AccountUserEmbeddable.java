package com.example.aerobankapp.embeddables;

import jakarta.persistence.Embeddable;
import lombok.*;

import java.io.Serializable;
import java.util.Objects;


@Getter
@Setter
@AllArgsConstructor(access = AccessLevel.PUBLIC)
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@Embeddable
public class AccountUserEmbeddable implements Serializable {

    private int acctID;
    private int userID;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AccountUserEmbeddable that = (AccountUserEmbeddable) o;
        return acctID == that.acctID && userID == that.userID;
    }

    @Override
    public int hashCode() {
        return Objects.hash(acctID, userID);
    }
}
