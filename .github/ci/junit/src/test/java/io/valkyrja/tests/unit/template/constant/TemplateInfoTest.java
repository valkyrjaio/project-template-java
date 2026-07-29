/*
 * This file is part of the Valkyrja Framework package.
 *
 * (c) Melech Mizrachi <melechmizrachi@gmail.com>
 *
 * For the full copyright and license information, please view the LICENSE
 * file that was distributed with this source code.
 */

package io.valkyrja.tests.unit.template.constant;

import static org.junit.jupiter.api.Assertions.assertNotEquals;

import io.valkyrja.template.constant.TemplateInfo;
import org.junit.jupiter.api.Test;

/** Tests for {@link TemplateInfo}. */
final class TemplateInfoTest {

    @Test
    void versionIsSet() {
        assertNotEquals("", TemplateInfo.VERSION);
    }

    @Test
    void versionBuildDateTimeIsSet() {
        assertNotEquals("", TemplateInfo.VERSION_BUILD_DATE_TIME);
    }
}
